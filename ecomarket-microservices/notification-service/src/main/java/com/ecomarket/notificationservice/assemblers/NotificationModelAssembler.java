package com.ecomarket.notificationservice.assemblers;

import com.ecomarket.notificationservice.controller.NotificationController;
import com.ecomarket.notificationservice.model.Notification;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class NotificationModelAssembler implements RepresentationModelAssembler<Notification, EntityModel<Notification>> {

    @Override
    public EntityModel<Notification> toModel(Notification notification) {
        return EntityModel.of(notification,
                linkTo(methodOn(NotificationController.class).getById(notification.getId())).withSelfRel(),
                linkTo(methodOn(NotificationController.class).getAllNotifications()).withRel("all-notifications"));
    }
}
