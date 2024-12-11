package ru.mephi.dozen.oop.lab3.service;

import ru.mephi.dozen.oop.lab3.TestDigitalSignatureService;
import ru.mephi.dozen.oop.lab3.service.impl.BlockingDocumentService;
import ru.mephi.dozen.oop.lab3.service.impl.ParallelDocumentService;

class ParallelDocumentServiceTest extends AbstractDocumentServiceTest {


    @Override
    protected DocumentService getDocumentService() {
        TestDigitalSignatureService object = new TestDigitalSignatureService();
        // TODO: когда будет готова реализация сервиса, надо будет поставить зависимости (как минимум new TestDigitalSignatureService())
        return new ParallelDocumentService(object);
    }
}