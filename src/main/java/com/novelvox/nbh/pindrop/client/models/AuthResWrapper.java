package com.novelvox.nbh.pindrop.client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResWrapper {
    private AuthRes data;
}
