package com.llqlv.restapp.controllers;

import com.llqlv.restapp.dto.PersonDto;
import com.llqlv.restapp.entity.Person;
import com.llqlv.restapp.service.PeopleService;
import com.llqlv.restapp.util.PersonErrorResponse;
import com.llqlv.restapp.util.PersonNotCreatedException;
import com.llqlv.restapp.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<PersonDto> getPeople() {
        return peopleService.findAll().stream().map(this::convertToPersonDto).collect(Collectors.toList()); // Jackson конвертирует все в JSON
    }

    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable("id") int id) {
        return convertToPersonDto(peopleService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDto personDto,
                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            var errorMsg = new StringBuilder();

            List<FieldError> fieldError = bindingResult.getFieldErrors();
            for (FieldError error : fieldError) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(errorMsg.toString());
        }

        peopleService.save(convertToPerson(personDto));

        //Отправляем HTTP ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException personNotFoundException) {
        PersonErrorResponse response = new PersonErrorResponse("Person with this id wasn't found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // - 404 status
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException personNotCreatedException) {
        PersonErrorResponse response = new PersonErrorResponse(personNotCreatedException.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 status
    }

    private Person convertToPerson(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }

    private PersonDto convertToPersonDto(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }
}
