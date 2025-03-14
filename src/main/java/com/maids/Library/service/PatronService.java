package com.maids.Library.service;

import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Book;
import com.maids.Library.model.Patron;
import com.maids.Library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    public List<Patron> findAll(){
        return patronRepository.findAll();
    }

    public Patron findById(Long id) throws ResourceNotFoundException{
        return patronRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patron Not Found"));
    }

    public Patron save(Patron patronDetails) {
        Patron patron = new Patron(patronDetails.getName(),patronDetails.getContactInformation());
        return patronRepository.save(patron);
    }

    public Patron update(Long id, Patron patronDetails) throws ResourceNotFoundException {
        Patron patron = findById(id);
        patron.setName(patronDetails.getName());
        patron.setContactInformation(patronDetails.getContactInformation());
        return patronRepository.save(patron);
    }

    public void delete(Long id) throws ResourceNotFoundException {
        patronRepository.delete(findById(id));
    }

}
