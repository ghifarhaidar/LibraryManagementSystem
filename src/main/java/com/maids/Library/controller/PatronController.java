package com.maids.Library.controller;

import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Patron;
import com.maids.Library.service.PatronService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    // GET /api/patrons: Retrieve a list of all patrons
    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        return ResponseEntity.ok(patronService.findAll());
    }

    // GET /api/patrons/{id}: Retrieve details of a specific patron by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(patronService.findById(id));
    }

    // POST /api/patrons: Add a new patron to the system
    @PostMapping
    public ResponseEntity<Patron> createPatron(@Valid @RequestBody Patron patron) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patronService.save(patron));
    }

    // PUT /api/patrons/{id}: Update an existing patron's information
    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id,@Valid @RequestBody Patron patronDetails) throws ResourceNotFoundException  {
        Patron updatedPatron = patronService.update(id, patronDetails);
        return ResponseEntity.ok(updatedPatron);
    }

    // DELETE /api/patrons/{id}: Remove a patron from the system
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) throws ResourceNotFoundException  {
        patronService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
