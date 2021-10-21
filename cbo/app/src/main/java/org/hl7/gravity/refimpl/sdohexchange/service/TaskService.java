package org.hl7.gravity.refimpl.sdohexchange.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.gravity.refimpl.sdohexchange.auth.AuthorizationClient;
import org.hl7.gravity.refimpl.sdohexchange.dao.ServerRepository;
import org.hl7.gravity.refimpl.sdohexchange.dao.TaskRepository;
import org.hl7.gravity.refimpl.sdohexchange.dto.converter.TaskBundleToDtoConverter;
import org.hl7.gravity.refimpl.sdohexchange.dto.response.TaskDto;
import org.hl7.gravity.refimpl.sdohexchange.exception.AuthClientException;
import org.hl7.gravity.refimpl.sdohexchange.model.Server;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskService {

  private static final String SCOPE = "address phone read profile openid email write";

  private final FhirContext fhirContext;
  private final ServerRepository serverRepository;
  private final AuthorizationClient authorizationClient;

  public TaskService(ServerRepository serverRepository, FhirContext fhirContext) {
    this.fhirContext = fhirContext;
    this.serverRepository = serverRepository;
    this.authorizationClient = new AuthorizationClient(new RestTemplate());
  }

  public List<TaskDto> getTasks() throws AuthClientException {
    List<Server> serverList = serverRepository.findAll();
    List<TaskDto> taskDtoList = new ArrayList<>();
    for (Server server : serverList) {
      IGenericClient fhirClient = fhirContext.newRestfulGenericClient(server.getFhirServerUrl());
//      fhirClient.registerInterceptor(new BearerTokenAuthInterceptor(
//          authorizationClient.getTokenResponse(URI.create(server.getAuthServerUrl()), server.getClientId(),
//                  server.getClientSecret(), SCOPE)
//              .getAccessToken()));
      TaskRepository taskRepository = new TaskRepository(fhirClient);
      Bundle bundle = taskRepository.findAllTasks();
      taskDtoList.addAll(new TaskBundleToDtoConverter().convert(bundle));
    }
    return taskDtoList;
  }
}
