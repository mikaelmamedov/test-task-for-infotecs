package com.mikaelmamadov.test.controller;


import com.mikaelmamadov.test.service.TtlHashMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TtlHashMapRestController {
    TtlHashMapService ttlHashMapService;

    @Autowired
    public TtlHashMapRestController(TtlHashMapService ttlHashMapService) {
        this.ttlHashMapService = ttlHashMapService;
    }

    @GetMapping("/get/{key}")
    public ResponseEntity<String> getElementByKey(@PathVariable Long key) {
        String result = ttlHashMapService.getElementByKey(key);
        if(result == null){
            return new ResponseEntity<>("couldnt find the element", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveElement(@RequestParam long key, @RequestParam String value, @RequestParam(required = false) Long timeout) {
        String val = ttlHashMapService.save(key, value, timeout);
        return new ResponseEntity<>(val, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/remove/{key}")
    public ResponseEntity<?> removeByKey(@PathVariable Long key){
        ttlHashMapService.removeByKey(key);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/dump")
    public ResponseEntity<?> dump(){
        ttlHashMapService.dump();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/load")
    public ResponseEntity<?> load() {
        ttlHashMapService.load();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
