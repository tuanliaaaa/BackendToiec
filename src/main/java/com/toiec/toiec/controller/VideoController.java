package com.toiec.toiec.controller;

import jakarta.annotation.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @GetMapping("/stream/{filename}")
    @CrossOrigin("*")
    @ResponseBody
    public  ResponseEntity<StreamingResponseBody> VideoController(
            @PathVariable("filename") String video_id,
            @RequestHeader(value = "Range", required = false) String rangeHeader)
    {
        try
        {
            StreamingResponseBody responseStream;
            String filePathString = video_id;
            Path filePath = Paths.get(filePathString);
            Long fileSize = Files.size(filePath);
            byte[] buffer = new byte[1024];
            final HttpHeaders responseHeaders = new HttpHeaders();

            if (rangeHeader == null)
            {
                responseHeaders.add("Content-Type", "video/mp4");
                responseHeaders.add("Content-Length", fileSize.toString());
                responseStream = os -> {
                    RandomAccessFile file = new RandomAccessFile(filePathString, "r");
                    try (file)
                    {
                        long pos = 0;
                        file.seek(pos);
                        while (pos < fileSize - 1)
                        {
                            file.read(buffer);
                            os.write(buffer);
                            pos += buffer.length;
                        }
                        os.flush();
                    } catch (Exception e) {}
                };

                return new ResponseEntity<StreamingResponseBody>
                        (responseStream, responseHeaders, HttpStatus.OK);
            }

            String[] ranges = rangeHeader.split("-");
            Long rangeStart = Long.parseLong(ranges[0].substring(6));
            Long rangeEnd;
            if (ranges.length > 1)
            {
                rangeEnd = Long.parseLong(ranges[1]);
            }
            else
            {
                rangeEnd = fileSize - 1;
            }

            if (fileSize < rangeEnd)
            {
                rangeEnd = fileSize - 1;
            }

            String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            responseHeaders.add("Content-Type", "video/mp4");
            responseHeaders.add("Content-Length", contentLength);
            responseHeaders.add("Accept-Ranges", "bytes");
            responseHeaders.add("Content-Range", "bytes" + " " +
                    rangeStart + "-" + rangeEnd + "/" + fileSize);
            final Long _rangeEnd = rangeEnd;
            responseStream = os -> {
                RandomAccessFile file = new RandomAccessFile(filePathString, "r");
                try (file)
                {
                    long pos = rangeStart;
                    file.seek(pos);
                    while (pos < _rangeEnd)
                    {
                        file.read(buffer);
                        os.write(buffer);
                        pos += buffer.length;
                    }
                    os.flush();
                }
                catch (Exception e) {}
            };

            return new ResponseEntity<StreamingResponseBody>
                    (responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
        }
        catch (FileNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
