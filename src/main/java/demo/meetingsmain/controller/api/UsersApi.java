package demo.meetingsmain.controller.api;

import demo.meetingscontracts.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Tag(name = "Users", description = "Операции с пользователями")
@RequestMapping("/users")
public interface UsersApi {

    @Operation(summary = "Получение пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Пользователь найден!")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден!")
    @GetMapping("/{id}")
    UserDTO findById(@PathVariable Integer id);

    @Operation(summary = "Получение всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping("/all")
    List<UserDTO> findAll();
}
