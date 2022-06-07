package com.techniners.EmailApp.services;

import com.techniners.EmailApp.data.models.MailBox;

public interface MailboxService {

    MailBox findMailbox (String UserName);
    void saveMailbox (MailBox mailBox);

}
