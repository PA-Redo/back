package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.exposition.dto.core.RoleAuthResponse;
import fr.croixrouge.exposition.dto.core.RoleCreationRequest;
import fr.croixrouge.exposition.dto.core.RoleResponse;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.LocalUnitService;
import fr.croixrouge.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController extends CRUDController<ID, Role, RoleService, RoleResponse, RoleCreationRequest> {

    private final AuthenticationService authenticationService;

    private final LocalUnitService localUnitService;

    public RoleController(RoleService roleService, AuthenticationService authenticationService, LocalUnitService localUnitService) {
        super(roleService);
        this.authenticationService = authenticationService;
        this.localUnitService = localUnitService;
    }

    @GetMapping("auth")
    public ResponseEntity<RoleAuthResponse> getAuths(){
        return ResponseEntity.ok(new RoleAuthResponse());
    }

    @Override
    public RoleResponse toDTO(Role model) {
        return new RoleResponse(model.getId().value(),
                model.getLocalUnit() == null ? null : model.getLocalUnit().getId().value(),
                model.getName(),
                model.getDescription(),
                model.getAuthorizations(),
                List.of());
    }

    @Override
    @PostMapping()
    public ResponseEntity<ID> post(@RequestBody RoleCreationRequest model) {
        var localUnit = localUnitService.findById( new ID(model.getLocalUnitID()) );
        return ResponseEntity.ok(service.save(model.toModel(localUnit)));
    }

    @GetMapping("/localunit/{id}")
    public ResponseEntity<List<RoleResponse>> getRolesFromLocalUnitId(@PathVariable ID id) {
        List<RoleResponse> roleResponse = service.getRoleByLocalUnitId(id).stream().map(RoleResponse::fromRole).collect(Collectors.toList());
        return ResponseEntity.ok(roleResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateRole(@PathVariable ID id, @RequestBody RoleCreationRequest roleCreationRequest) {
        service.updateRole(id, roleCreationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{roleId}/user/{userId}")
    public ResponseEntity<Void> addRoleToUser(@PathVariable ID roleId, @PathVariable ID userId) {
        service.addRole(roleId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{roleId}/user/{userId}")
    public ResponseEntity<Void> removeRoleToUser(@PathVariable ID roleId, @PathVariable ID userId) {
        service.removeRole(roleId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/users")
    public ResponseEntity<List<Long>> getAllByRole(@PathVariable ID id) {
        return ResponseEntity.ok( service.getAllByRole(id).stream().map(user -> user.getId().value()).toList());
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<RoleResponse>> getUserRole( @PathVariable ID userId) {
        return ResponseEntity.ok(service.getUserRole( userId).stream().map(RoleResponse::fromRole).toList());
    }

    @GetMapping("user/auths")
    public ResponseEntity<Map<Resources, Set<Operations>>> getUserRoleByToken(HttpServletRequest request) {
        ID userId = authenticationService.getUserIdFromJwtToken(request);
        return ResponseEntity.ok(service.getUserAuthorizations(userId));
    }
}
