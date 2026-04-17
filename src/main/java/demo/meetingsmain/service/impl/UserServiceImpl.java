package demo.meetingsmain.service.impl;

import demo.meetingscontracts.dto.UserDTO;
import demo.meetingscontracts.exceptions.ResourceNotFoundException;
import demo.meetingsmain.domain.User;
import demo.meetingsmain.repository.UserRepository;
import demo.meetingsmain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDTO findById(Integer id) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            return toUserDTO(optUser.get());
        } else {
            throw new ResourceNotFoundException("User", id);
        }
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    private UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic()
        );
    }
}
