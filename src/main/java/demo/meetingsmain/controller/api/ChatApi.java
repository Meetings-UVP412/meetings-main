package demo.meetingsmain.controller.api;

import demo.meetingscontracts.dto.ChatDTO;
import demo.meetingscontracts.dto.ChatRequest;
import demo.meetingscontracts.dto.MessageRequest;
import demo.meetingscontracts.dto.UpdateMessagesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chats", description = "Создание/удаление чатов и отправка сообщений к LLM")
@RequestMapping("/chats")
public interface ChatApi {

    @Operation(summary = "Получение всех чатов")
    @GetMapping("/all")
    List<ChatDTO> getAllChats();

    @Operation(summary = "Получение всех чатов по UUID встречи")
    @GetMapping("/{meetingUUID}")
    List<ChatDTO> getChatsForMeeting(@PathVariable String meetingUUID);

    @Operation(summary = "Создание чата")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "404", description = "Встреча на найдена!")
    @PostMapping("/create")
    ChatDTO createChat(@RequestBody ChatRequest request);

    @Operation(summary = "Отправка сообщения")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> sendMessage(@RequestBody MessageRequest request);

    @Operation(summary = "История чата")
    @GetMapping("/history/{chatUUID}")
    ChatDTO chatHistory(@PathVariable String chatUUID);

    @Operation(summary = "Удаление чата")
    @DeleteMapping("/{chatUUID}")
    ResponseEntity<String> deleteChat(@PathVariable String chatUUID);

    @PostMapping("/{chatUUID}/update-messages")
    ResponseEntity<String> updateChatMessages(@PathVariable String chatUUID,@RequestBody UpdateMessagesRequest request);
}
