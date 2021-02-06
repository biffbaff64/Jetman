
package com.richikin.jetman.maps;

public class Room
{
    public String   roomName;
    public MapEntry mapEntry;

    public Room()
    {
        this("", new MapEntry());
    }

    public Room(final String name, MapEntry entry)
    {
        this.roomName = name;
        this.mapEntry = entry;
    }

    public void set(Room reference)
    {
        this.roomName = reference.roomName;
    }
}
