package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.UserDTO;
import demo.meetingsmain.controller.api.UsersApi;
import demo.meetingsmain.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController implements UsersApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDTO findById(Integer id) {
        return userService.findById(id);
    }

    @Override
    public List<UserDTO> findAll() {
        return userService.findAll();
    }
}
