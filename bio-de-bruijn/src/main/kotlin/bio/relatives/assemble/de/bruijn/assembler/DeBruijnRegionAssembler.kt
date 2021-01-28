package bio.relatives.assemble.de.bruijn.assembler

import bio.relatives.assemble.de.bruijn.analyzer.DeBruijnGraphAnalyzerImpl
import bio.relatives.assemble.de.bruijn.builder.DeBruijnGraphBuilderImpl
import bio.relatives.assemble.de.bruijn.selector.NucleotideSequenceSelectorImpl
import bio.relatives.assemble.de.bruijn.sorter.SAMRecordsSorterImpl
import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware
import htsjdk.samtools.SAMRecord

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
class DeBruijnRegionAssembler : RegionAssembler {
    override fun assemble(feature: Feature, records: List<SAMRecord>): Region {
        val kMerSize = 3
        val recordsSorter = SAMRecordsSorterImpl()
        val graphBuilder = DeBruijnGraphBuilderImpl()
        val graphAnalyzer = DeBruijnGraphAnalyzerImpl()
        val sequenceSelector = NucleotideSequenceSelectorImpl()

        val nucleotideSequence = sequenceSelector.select(
                graphAnalyzer.analyze(
                        graphBuilder.buildGraph(
                                recordsSorter.sort(records), kMerSize
                        )
                ), feature
        )

        TODO("change role to the natural role")
        return Region(RoleAware.Role.FATHER, nucleotideSequence.nucleotides, feature.chromosome, feature.gene, feature.start, feature.end)
    }
}