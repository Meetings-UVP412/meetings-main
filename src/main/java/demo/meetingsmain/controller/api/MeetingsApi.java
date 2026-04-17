package demo.meetingsmain.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Tag(name = "Meeting", description = "Встречи")
@RequestMapping("/api/meetings")
public interface MeetingsApi {

    @Operation(summary = "Получение встречи по id")
    @ApiResponse(responseCode = "200", description = "Информация о встрече")
    @ApiResponse(responseCode = "404", description = "Встреча на найдена!")
    @GetMapping("/{uuid}")
    MeetingResponse getMeeting(@PathVariable UUID uuid);

    @Operation(summary = "Получение всех встреч")
    @ApiResponse(responseCode = "200", description = "Все встречи")
    @GetMapping()
    List<MeetingResponse> getMeetings();

    @Operation(summary = "Создание встречи")
    @ApiResponse(responseCode = "201", description = "Встреча успешно создана!")
    @ApiResponse(responseCode = "404", description = "Автор/участники не найдены!")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    MeetingResponse createMeeting(@RequestBody MeetingRequest request);

    @Operation(summary = "Загрузка аудио файла")
    @ApiResponse(responseCode = "201", description = "Файл успешно загружен!")
    @ApiResponse(responseCode = "400", description = "Встреча должна быть новой!")
    @ApiResponse(responseCode = "404", description = "Встреча не найдена!")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/uploadFile")
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                      @RequestParam("ord") Integer ord,
                                      @RequestParam("isLast") Boolean isLast,
                                      @RequestParam("m-uid") UUID uuid,
                                      @RequestParam("duration") int duration
    );

    @Operation(summary = "Удаление транскрипции и аудио файлов")
    @ApiResponse(responseCode = "200", description = "Транскрипции и аудио файлы успешно очищены!")
    @ApiResponse(responseCode = "404", description = "Встреча не найдена!")
    @PostMapping("/drop/{meetingUUID}")
    MeetingResponse dropTranscription(@PathVariable String meetingUUID) throws JsonProcessingException;
}
