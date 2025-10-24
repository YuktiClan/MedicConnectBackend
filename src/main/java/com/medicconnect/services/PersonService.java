package com.medicconnect.services;

import com.medicconnect.dto.PersonDTO;
import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import com.medicconnect.models.Status;
import com.medicconnect.repositories.OrganizationRepository;
import com.medicconnect.repositories.PersonRepository;
import com.medicconnect.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PersonService(PersonRepository personRepository,
                         OrganizationRepository organizationRepository,
                         BCryptPasswordEncoder passwordEncoder,
                         EmailService emailService) {
        this.personRepository = personRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // ---------------- Create Main Admin ----------------
    @Transactional
    public Person createMainAdmin(PersonDTO dto) {
        Organization org = organizationRepository.findByOrgId(dto.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Person admin = dto.toPerson(org);
        admin.setRoles(List.of("MAIN_ADMIN"));
        admin.setStatus(Status.APPROVED);
        admin.setAgreement(true);
        admin.setAssociatedDate(LocalDateTime.now());
        admin.setPassword(encodePassword(admin.getPassword()));

        ValidationUtils.validateEmail(admin.getEmail());
        Person savedAdmin = personRepository.save(admin);

        sendEmail(admin.getEmail(),
                "Admin Account Registered",
                emailService.generatePersonRegistrationSuccessEmail(
                        admin.getName(),
                        admin.getUserId(),
                        admin.getEmail(),
                        org.getName(),
                        org.getOrgId(),
                        org.getCategory(),
                        admin.getAssociatedDate()
                )
        );

        return savedAdmin;
    }

    // ---------------- Create Regular User (Pending Approval) ----------------
    @Transactional
    public Person createUser(PersonDTO dto) {
        Organization org = organizationRepository.findByOrgId(dto.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Person user = dto.toPerson(org);
        user.setRoles(dto.getRoles());
        user.setStatus(Status.PENDING);
        user.setAgreement(true);
        user.setAssociatedDate(LocalDateTime.now());
        user.setPassword(encodePassword(user.getPassword()));

        Person savedUser = personRepository.save(user);

        notifyMainAdminForApproval(savedUser);
        notifyUserPendingRegistration(savedUser);

        return savedUser;
    }

    // ---------------- Approve User ----------------
    @Transactional
    public Person approveUser(Long userId) {
        Person user = findById(userId);
        user.setStatus(Status.APPROVED);
        Person approvedUser = personRepository.save(user);

        notifyUserApprovalCompleted(approvedUser);
        return approvedUser;
    }

    // ---------------- Fetch Pending Users ----------------
    public List<Person> getPendingUsersByOrg(String orgId) {
        return personRepository.findAll().stream()
                .filter(p -> p.getOrganization() != null
                        && orgId.equals(p.getOrganization().getOrgId())
                        && p.getStatus() == Status.PENDING)
                .collect(Collectors.toList());
    }

    // ---------------- Notification Helpers ----------------
    private void notifyMainAdminForApproval(Person user) {
        if (user.getOrganization() == null || user.getRoles() == null) return;

        Optional<Person> adminOpt = personRepository.findAll().stream()
                .filter(p -> p.getOrganization().getOrgId().equals(user.getOrganization().getOrgId())
                        && p.getRoles().contains("MAIN_ADMIN"))
                .findFirst();

        adminOpt.ifPresent(admin -> sendEmail(admin.getEmail(),
                "New User Pending Approval",
                emailService.generateNewUserNotificationForAdmin(
                        admin.getName(),
                        user.getName(),
                        user.getEmail(),
                        user.getUserId(),
                        user.getRoles(),
                        user.getOrganization().getName(),
                        user.getOrganization().getOrgId(),
                        user.getOrganization().getCategory(),
                        user.getAssociatedDate()
                )
        ));
    }

    private void notifyUserPendingRegistration(Person user) {
        sendEmail(user.getEmail(),
                "Registration Successful - Pending Approval",
                emailService.generateUserRegistrationPendingEmail(
                        user.getName(),
                        user.getUserId(),
                        user.getEmail(),
                        user.getRoles(),
                        user.getOrganization().getName(),
                        user.getOrganization().getOrgId(),
                        user.getOrganization().getCategory(),
                        user.getAssociatedDate()
                )
        );
    }

    private void notifyUserApprovalCompleted(Person user) {
        sendEmail(user.getEmail(),
                "Account Approved - Medic-connect",
                emailService.generateUserApprovedEmail(
                        user.getName(),
                        user.getUserId(),
                        user.getEmail(),
                        user.getRoles(),
                        user.getOrganization().getName(),
                        user.getOrganization().getOrgId(),
                        user.getOrganization().getCategory(),
                        LocalDateTime.now()
                )
        );
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            emailService.sendEmail(to, subject, body);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    // ---------------- Password Helper ----------------
    private String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.startsWith("$2a$")) return rawPassword;
        return passwordEncoder.encode(rawPassword);
    }

    // ---------------- Standard CRUD ----------------
    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public Person updatePerson(Long id, PersonDTO dto, Organization org) {
        Person person = findById(id);

        if (dto.getName() != null) person.setName(dto.getName());
        if (dto.getEmail() != null) person.setEmail(dto.getEmail());
        if (dto.getMobile() != null) person.setMobile(dto.getMobile());
        if (dto.getGender() != null) person.setGender(dto.getGender());
        if (dto.getBloodGroup() != null) person.setBloodGroup(dto.getBloodGroup());
        if (org != null) person.setOrganization(org);
        if (dto.getDocuments() != null) person.setDocuments(dto.getDocuments());
        if (dto.getRoles() != null) person.setRoles(dto.getRoles());
        if (dto.getPassword() != null) person.setPassword(encodePassword(dto.getPassword()));

        return personRepository.save(person);
    }

    @Transactional
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    public Optional<Person> findByEmailOrUserId(String emailOrUserId) {
        return personRepository.findByEmailOrUserId(emailOrUserId);
    }
public Person rejectUser(Long userId) {
    Person user = findById(userId);
    user.setStatus(Status.REJECTED);
    Person rejectedUser = personRepository.save(user);

    sendEmail(user.getEmail(),
              "Account Rejected - Medic-connect",
              emailService.generateUserRejectedEmail(user.getName(), user.getUserId(), user.getEmail(), user.getRoles(),
                                                     user.getOrganization().getName(), user.getOrganization().getOrgId(),
                                                     user.getOrganization().getCategory(), LocalDateTime.now()));
    return rejectedUser;
}

}
