package com.dxc.minifilesharing.file.exception;

import com.dxc.minifilesharing.file.common.FileServiceError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileServiceException extends RuntimeException {

    private static final long serialVersionUID = 5400022041190915190L;
    private final FileServiceError response;
    private final transient List<Object> parameters = new ArrayList<>();

    public FileServiceException(FileServiceError response) {
        this(response, null, new Object[0]);
    }

    public FileServiceException(FileServiceError response, Object... params) {
        this(response, null, params);
    }

    public  FileServiceException(FileServiceError response, Throwable cause, Object... params) {
        super(response.name() + Arrays.stream(params).map(Object::toString).collect(Collectors.joining(",","[","]")), cause);
        this.response = response;
        Collections.addAll(this.parameters, params);
    }

    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public void addParameters(Object param) {
        this.parameters.add(param);
    }

    public FileServiceError getResponse() {
        return response;
    }
}
