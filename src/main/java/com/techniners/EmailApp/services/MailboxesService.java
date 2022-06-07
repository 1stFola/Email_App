package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.Mailboxes;
import com.techniners.EmailApp.exceptions.EmailAppException;


public interface MailboxesService {

    Mailboxes findMailboxes(String userName) throws EmailAppException;
    void saveMailBoxes(Mailboxes mailboxes);

}

