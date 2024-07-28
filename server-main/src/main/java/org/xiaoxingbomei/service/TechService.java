package org.xiaoxingbomei.service;

import org.xiaoxingbomei.entity.GlobalEntity;

public interface TechService
{

    public GlobalEntity mongodbOfInsert(String paramString);

    public GlobalEntity mongodbOfMultiInsert(String paramString);

    public GlobalEntity mongodbOfSave(String paramString);

    public GlobalEntity mongodbOfSearch(String paramString);

    public GlobalEntity mongodbOfUpdate(String paramString);

    public GlobalEntity mongodbOfDelete(String paramString);

}
