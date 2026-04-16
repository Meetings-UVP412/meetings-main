package demo.meetingsmain.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Tag(name = "Redis", description = "Доступ к Redis")
@RequestMapping("/internal")
public interface RedisApi {

    @Operation(summary = "Обновление транскрипции встречи")
    @ApiResponse(responseCode = "201", description = "Текст успешно обновлен!")
    @PatchMapping("/updateText/{uuid}")
    ResponseEntity<String> updateTranscriptionForMeeting(@PathVariable UUID uuid, @RequestBody String body);

    @Operation(summary = "Получение транксрипции встречи")
    @ApiResponse(responseCode = "200", description = "Транскрипция получена!")
    @GetMapping("/meetingResult/{uuid}")
    String getMeetingTranscription(@PathVariable UUID uuid);

    @Operation(summary = "Получение аудио файла")
    @ApiResponse(responseCode = "200", description = "Файл получен!")
    @GetMapping("/{uuid}/chunks/{ord}")
    ResponseEntity<byte[]> getAudioChunk(@PathVariable UUID uuid, @PathVariable Integer ord);
}
