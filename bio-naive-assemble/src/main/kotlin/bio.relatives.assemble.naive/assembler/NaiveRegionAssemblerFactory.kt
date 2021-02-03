package bio.relatives.assemble.naive.assembler

import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.assembler.RegionAssemblerFactory
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@Component("NaiveRegionAssemblerFactory")
class NaiveRegionAssemblerFactory : RegionAssemblerFactory {
    override fun create(): RegionAssembler = NaiveRegionAssembler()
}