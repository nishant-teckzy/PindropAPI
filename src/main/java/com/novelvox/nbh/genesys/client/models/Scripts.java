package com.novelvox.nbh.genesys.client.models;

import lombok.Data;

@Data
public class Scripts {
    public Integer pageSize;
    public Integer pageNumber;
    public Integer total;
    public String lastUri;
    public String firstUri;
    public String selfUri;
    public Integer pageCount;
}
