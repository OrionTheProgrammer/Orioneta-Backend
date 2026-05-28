package cl.orioneta.conversations.domain.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Conversation {

    // Atributos
    private UUID id;
    private String title; // titulo del grupo o de la persona
    private String type; // Tipo GRUPO o DIRECTO, para 1 persona o para un grupo de 1 a mas personas
    private String avatar_url; // url de la foto del grupo o usuario
    private Participant participant; // Participantes dentro de la conversacion
    private Timestamp created_by; // fecha y hora de Conversacion creada alguien
    private Timestamp created_at; // fecha y hora de creacion
    private Timestamp updated_at; // fecha y hora de ultima actualizacion





}
