package dev.mim1q.structuredev

const val LEGAL_CHARS = "[a-zA-Z0-9_:]+"
val ELEMENT_FILTER = "^${LEGAL_CHARS}\$".toRegex()
val CHILD_FILTER = "^${LEGAL_CHARS}(/${LEGAL_CHARS})+\$".toRegex()

class IdentifierTree<T>(
    map: Map<String, T>,
    private val prefix: String = ""
) {
    private val children: Map<String, IdentifierTree<T>>
    private val elements: Map<String, T>

    init {
        val childrenMap = mutableMapOf<String, IdentifierTree<T>>()

        this.elements = map.filter { it.key.matches(ELEMENT_FILTER) }
        val topLevels = map.filter { it.key.matches(CHILD_FILTER) }
            .map { it.key.substringBefore('/') }
            .toSet()

        topLevels.forEach { key ->
            childrenMap[key] = IdentifierTree(map
                .filter { it.key.startsWith("$key/") }
                .mapKeys { it.key.removePrefix("$key/") },
                prefix = "$prefix$key/"
            )
        }
        this.children = childrenMap.toMap()
    }

    fun traverse(
        treeFunction: (key: String, tree: IdentifierTree<T>) -> Unit = { _, _ -> },
        elementFunction: (key: String, value: T) -> Unit,
    ) {
        treeFunction(prefix, this)
        elements.forEach { elementFunction("$prefix${it.key}", it.value) }
        children.forEach { it.value.traverse(treeFunction, elementFunction) }
    }

    fun streamImmediateElements(
    ) = elements.map { "$prefix${it.key}" to it.value }
        .stream()
}