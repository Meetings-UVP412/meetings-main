package demo.meetingsmain.service;

import demo.meetingscontracts.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO findById(Integer id);
    List<UserDTO> findAll();
}
