package bio.relatives.common.assembler

/**
 * @author shvatov
 */
interface RegionAssemblerFactory {
    fun create(): RegionAssembler
}