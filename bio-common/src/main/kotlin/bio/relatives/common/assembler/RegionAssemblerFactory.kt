package bio.relatives.common.assembler

/**
 * @author shvatov
 */
interface RegionAssemblerFactory {
    /**
     * Creates an instance of the [RegionAssembler] defined in the plugged in module.
     */
    fun create(): RegionAssembler
}