
package com.richikin.jetman.maps;

public class Room
{
    public String   roomName;
    public MapEntry mapEntry;

    public Room()
    {
        this("", new MapEntry());
    }

    public Room(final String _roomName, MapEntry _mapEntry)
    {
        this.roomName = _roomName;
        this.mapEntry = _mapEntry;
    }

    public void set(Room _reference)
    {
        this.roomName = _reference.roomName;
    }
}
