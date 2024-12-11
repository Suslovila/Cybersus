package ru.mephi.dozen.oop.lab3.service;

import ru.mephi.dozen.oop.lab3.TestDigitalSignatureService;
import ru.mephi.dozen.oop.lab3.service.impl.BlockingDocumentService;

class BlockingDocumentServiceTest extends AbstractDocumentServiceTest {

    @Override
    protected DocumentService getDocumentService() {
        return new BlockingDocumentService(new TestDigitalSignatureService());
    }
}