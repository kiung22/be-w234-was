package service;

import http.HttpRequest;
import http.HttpResponse;

public interface Service {

    HttpResponse run(HttpRequest httpRequest);
}
