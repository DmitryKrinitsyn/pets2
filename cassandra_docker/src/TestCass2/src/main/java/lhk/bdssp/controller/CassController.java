package lhk.bdssp.controller;

import lhk.bdssp.beans.Book;
import lhk.bdssp.model.StatusType;
import lhk.bdssp.model.StorageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by dmitry on 2/27/17.
 */


@RestController
@RequestMapping("/cass")
public class CassController {
    @Autowired
    StorageModel model;

    @RequestMapping(path = "/getbyname/{name}")
    List<Book> getBookByName(@PathVariable("name") String name) {
        return model.searchBookByName(name);
    }

    @RequestMapping(path = "/getbytext/{text}")
    List<Book> getBookByText(@PathVariable("text") String text) {
        return model.searchBookByContent(text);
    }

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file){

        String name = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                StatusType status = model.storeBook( new Book( name, new String(file.getBytes())));

                if(status == StatusType.failed)
                {
                    return "You failed to store " + name;
                }

            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }

        return "File " + name + " uploaded";
    }
}
