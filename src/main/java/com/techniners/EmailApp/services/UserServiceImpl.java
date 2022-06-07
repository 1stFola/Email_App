package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.*;
import com.techniners.EmailApp.data.repositories.UserRepository;
import com.techniners.EmailApp.dtos.requests.userCenteredRequests.AccountCreationRequest;
import com.techniners.EmailApp.dtos.UserDTO;
import com.techniners.EmailApp.exceptions.EmailAppException;
import com.techniners.EmailApp.exceptions.UserNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    @Autowired
    private MailboxesServiceImpl mailboxesService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    NotificationServiceImpl notificationService;

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Map<String, User> userAccountRepository = new HashMap<>();


    @Override
    public UserDTO createAccount(AccountCreationRequest acctCreatRequest) throws EmailAppException {
        Optional<User> user = userRepository.findByUserName(acctCreatRequest.getUserName());
        if (user.isPresent())
        {
            throw new EmailAppException("User Name Already Exist");
        }
        log.info("encoding pass -> {}", acctCreatRequest.getPassword());
        User newUser = new User(acctCreatRequest.getUserName(),
                acctCreatRequest.getFirstName(),
                acctCreatRequest.getLastName(),
                passwordEncoder.encode(acctCreatRequest.getPassword()),
                LocalDateTime.now());
        newUser.setUserName(newUser.getUserName()+ "@fmail.com");
        Role role = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        newUser.addRole(role);
        User savedUser = userRepository.save(newUser);

        Mailboxes mailboxes = new Mailboxes(savedUser.getId(), savedUser.getUserName(), new HashMap<>());
        mailboxesService.saveMailBoxes(mailboxes);

        Notification notification = new Notification();
        notification.setEmailAddress(savedUser.getUserName());
        notificationService.saveNotification(notification);

        userAccountRepository.put(savedUser.getUserName(), savedUser);

        return mapper.map(savedUser, UserDTO.class);
    }

    @Override
    public User findByUserName(String userName) throws UserNotFoundException {
        Optional<User> foundUser = userRepository.findByUserName(userName);
        User userFound = foundUser.get();
        return userFound;

    }

    private boolean isExisting(String userName) {
        return userAccountRepository.values().stream().anyMatch(user -> user.getUserName().equalsIgnoreCase(userName));
    }


    @Override
    public void deleteUser(String userId) throws EmailAppException {
        User user = userRepository.findByUserName(userId).orElseThrow(()-> new EmailAppException("User not found"));
        userRepository.delete(user);
    }


    public void clear() {
        userRepository.deleteAll();
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUserName(String userName) throws EmailAppException {
        User user = userAccountRepository.getOrDefault(userName, null);
        if (user == null) {
            throw new EmailAppException("Invalid user");
        }
        return user;
    }


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("load user by username called");
        User user = userRepository.findByUserName(email).orElseThrow(()-> new UserNotFoundException("User does not exist"));
        log.info("found user --> {}", user);
        org.springframework.security.core.userdetails.User returnedUser = new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthorities(user.getRoles()));
        log.info("user details --> {}", returnedUser);
        return returnedUser;
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        Collection<? extends SimpleGrantedAuthority> authorities = roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getRoleType().name())
        ).collect(Collectors.toSet());
        return authorities;
    }


}
