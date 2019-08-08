package ru.capjack.tool.io

import ru.capjack.tool.lang.make

class BufferUnderflowException(message: String) : RuntimeException(message) {
	constructor(requestedSize: Int, availableSize: Int, readingBack: Boolean = false) : this(
		readingBack.make(
			"Not enough data to back read (requested: $requestedSize, available: $availableSize)",
			"Not enough data to read (requested: $requestedSize, available: $availableSize)"
		)
	)
	
	constructor(negativeSize: Int, readingBack: Boolean = false) : this(
		readingBack.make(
			"Back reading size is negative ($negativeSize)",
			"Reading size is negative ($negativeSize)"
		)
	)
	
}
