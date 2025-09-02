package br.com.gabriel.zlschat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import br.com.gabriel.zlschat.config.AuthenticationUserProvider;
import br.com.gabriel.zlschat.dtos.UserDTO;
import br.com.gabriel.zlschat.exceptions.UserAlreadyExistsException;
import br.com.gabriel.zlschat.exceptions.UserCannotBeAddedException;
import br.com.gabriel.zlschat.exceptions.UserNotFoundException;
import br.com.gabriel.zlschat.models.UserEntity;
import br.com.gabriel.zlschat.repositories.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationUserProvider authenticationUserProvider;
    @Mock
    private ChatRoomService chatRoomService;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should register user when email is not same of other users")
    public void registerSuccess() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("testemail@email.com");
        userDTO.setUsername("testusername");
        userDTO.setPassword("testpassword");

        when(this.userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(this.passwordEncoder.encode(userDTO.getPassword())).thenReturn("testPasswordHashed");
        this.userService.register(userDTO);

        ArgumentCaptor<UserEntity> argumentCaptorUserEntity = ArgumentCaptor.forClass(UserEntity.class);
        verify(this.userRepository).save(argumentCaptorUserEntity.capture());
        assertEquals("testPasswordHashed", argumentCaptorUserEntity.getValue().getPassword());
    }

    @Test
    @DisplayName("Should throw exception when email is same of other users")
    public void registerFail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("testemail@email.com");
        userDTO.setUsername("testusername");
        userDTO.setPassword("testpassword");

        when(this.userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> this.userService.register(userDTO));
        verify(this.userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Shold not return myself in findByUsername")
    public void findByUsernameButNotReturnMyself() {
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testusername");
        testUser.setPassword("testpassword");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn("testusername");
        when(this.userRepository.findByUsername("testusername")).thenReturn(Optional.of(testUser));
        when(this.userRepository.findByUsernameStartingWith("tes")).thenReturn(List.of(testUser));
        
        assertEquals(List.of(), this.userService.findByUsername("tes"));
    }

    @Test
    @DisplayName("Should get authenticated user")
    public void getAuthenticatedUserSuccess() {
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testusername");
        testUser.setPassword("testpassword");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn("testusername");
        when(this.userRepository.findByUsername("testusername")).thenReturn(Optional.of(testUser));

        assertEquals(testUser, this.userService.me());
    }

    @Test
    @DisplayName("Should throw exception when get authenticated user fail")
    public void getAuthenticatedUserFail() {
        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> this.userService.me());
    }

    @Test
    @DisplayName("Should request friendship")
    public void requestFriendshipSuccess() {
        UserEntity userReceiverEntity = new UserEntity();
        userReceiverEntity.setUsername("testusernamereceiver");

        UserEntity userSenderEntity = new UserEntity();
        userSenderEntity.setUsername("testusernamesender");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(userSenderEntity.getUsername());
        when(this.userRepository.findByUsername(userSenderEntity.getUsername())).thenReturn(Optional.of(userSenderEntity));
        when(this.userRepository.findByUsername(userReceiverEntity.getUsername())).thenReturn(Optional.of(userReceiverEntity));

        this.userService.requestFriendship(userReceiverEntity.getUsername());

        verify(this.userRepository).save(userReceiverEntity);
        assertEquals(1, userReceiverEntity.getFriendRequests().size());
        assertEquals(userSenderEntity.getUsername(), userReceiverEntity.getFriendRequests().get(0));
    }

    @Test
    @DisplayName("Should throw exception when request friendship to myself")
    public void requestFriendshipToMyselfFail() {
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testusername");
        testUser.setPassword("testpassword");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(testUser.getUsername());
        when(this.userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        assertThrows(RuntimeException.class, () -> this.userService.requestFriendship(testUser.getUsername()));
    }

    @Test
    @DisplayName("Should throw exception when request friendship username not found or not exists")
    public void requestFriendshipUsernameNotFoundOrNotExistsFail() {
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testusername");
        testUser.setPassword("testpassword");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(testUser.getUsername());
        when(this.userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(this.userRepository.findByUsername("testusernamenotfound")).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> this.userService.requestFriendship("testusernamenotfound"));
    }

    @Test
    @DisplayName("Should accept friendship")
    public void acceptFriendshipSuccess() {
        UserEntity userSenderEntity = new UserEntity();
        userSenderEntity.setUsername("testusernamesender");

        UserEntity userReceiverEntity = new UserEntity();
        userReceiverEntity.setUsername("testusernamereceiver");
        userReceiverEntity.getFriendRequests().add(userSenderEntity.getUsername());

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(userReceiverEntity.getUsername());
        when(this.userRepository.findByUsername(userSenderEntity.getUsername())).thenReturn(Optional.of(userSenderEntity));
        when(this.userRepository.findByUsername(userReceiverEntity.getUsername())).thenReturn(Optional.of(userReceiverEntity));
        
        this.userService.acceptFriendship(userSenderEntity.getUsername());
        
        verify(this.chatRoomService).createPrivateChatRoom(userSenderEntity.getUsername());
        verify(this.userRepository).save(userReceiverEntity);
        verify(this.userRepository).save(userSenderEntity);
        assertEquals(1, userSenderEntity.getFriends().size());
        assertEquals(1, userReceiverEntity.getFriends().size());
        assertEquals(userReceiverEntity.getUsername(), userSenderEntity.getFriends().get(0));
        assertEquals(userSenderEntity.getUsername(), userReceiverEntity.getFriends().get(0));
    }

    @Test
    @DisplayName("Should throw exception when accept friendship username not found or not exists")
    public void acceptFriendshipUsernameNotFoundInRequestFriendshipListFail() {
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testusername");
        testUser.setPassword("testpassword");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(testUser.getUsername());
        when(this.userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        
        assertThrows(UserCannotBeAddedException.class, () -> this.userService.acceptFriendship("testusernamenotfound"));
    }

    @Test
    @DisplayName("Should throw exception when accept friendship username found in request friendship list but not exists")
    public void acceptFriendshipUsernameFoundInRequestFriendshipListButNotExistsFail() {
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testusername");
        testUser.setPassword("testpassword");
        testUser.getFriendRequests().add("testusernamenotfound");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(testUser.getUsername());
        when(this.userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        
        assertThrows(UserNotFoundException.class, () -> this.userService.acceptFriendship("testusernamenotfound"));
        assertEquals(0, testUser.getFriendRequests().size());
    }

    @Test
    @DisplayName("Should reject friendship")
    public void rejectFriendshipSuccess() {
        UserEntity receiverUser = new UserEntity();
        receiverUser.setUsername("receiverusername");
        receiverUser.setPassword("testpassword");
        receiverUser.getFriendRequests().add("senderusername");

        UserEntity senderUser = new UserEntity();
        senderUser.setUsername("senderusername");
        senderUser.setPassword("testpassword");

        when(this.authenticationUserProvider.getUsernameOfAuthenticatedUser()).thenReturn(receiverUser.getUsername());
        when(this.userRepository.findByUsername(receiverUser.getUsername())).thenReturn(Optional.of(receiverUser));
        when(this.userRepository.findByUsername(senderUser.getUsername())).thenReturn(Optional.of(senderUser));

        this.userService.rejectFriendship(senderUser.getUsername());

        verify(this.userRepository).save(receiverUser);
        assertEquals(0, receiverUser.getFriendRequests().size());
    }
}
