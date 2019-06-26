package ru.capjack.tool.io

class BufferReadingException(requested: Int, available: Int) : IllegalStateException("Not enough data to read (requested: $requested, available: $available)")