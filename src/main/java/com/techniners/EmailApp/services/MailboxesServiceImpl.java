package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.Mailboxes;
import com.techniners.EmailApp.data.repositories.MailboxesRepository;
import com.techniners.EmailApp.exceptions.EmailAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



//@AllArgsConstructor
//@NoArgsConstructor
//@Validated
@Service
public class MailboxesServiceImpl implements MailboxesService {

    @Autowired
    private MailboxesRepository mailboxesRepository;


    @Override
    public Mailboxes findMailboxes(String userName) throws EmailAppException {

        Mailboxes mailboxes = mailboxesRepository.findByUserName(userName).orElseThrow(()-> new EmailAppException("Mailboxes not found"));

        return mailboxes;

    }

    @Override
    public void saveMailBoxes(Mailboxes mailboxes) {
        mailboxesRepository.save(mailboxes);
    }

    public void deleteMailboxes(String userName) throws EmailAppException {
        Mailboxes mailboxes = mailboxesRepository.findByUserName(userName).orElseThrow(() -> new EmailAppException("User not found"));
        mailboxesRepository.delete(mailboxes);
    }

    public void clear() {
        mailboxesRepository.deleteAll();
    }
}
