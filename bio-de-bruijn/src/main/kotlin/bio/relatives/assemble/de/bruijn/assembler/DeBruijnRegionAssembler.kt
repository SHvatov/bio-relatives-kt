package bio.relatives.assemble.de.bruijn.assembler

import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import htsjdk.samtools.SAMRecord

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
class DeBruijnRegionAssembler : RegionAssembler {
    override fun assemble(feature: Feature, records: List<SAMRecord>): Region {
        TODO()
    }
}