package bio.relatives.common.assembler

/**
 * @author shvatov
 */
interface RegionAssemblerFactory {
    /**
     * Creates an instance of the [RegionAssembler] defines in the plugged in module.
     */
    fun create(): RegionAssembler
}