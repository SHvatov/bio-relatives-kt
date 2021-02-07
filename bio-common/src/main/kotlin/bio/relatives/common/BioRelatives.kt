package bio.relatives.common

import bio.relatives.common.cli.parser.ArgParserFactory
import bio.relatives.common.cli.runner.Runner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Start point for our application
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@SpringBootApplication
open class BioRelatives : CommandLineRunner {
    @Autowired
    private lateinit var argParserFactory: ArgParserFactory

    @Autowired
    private lateinit var runner: Runner

    override fun run(args: Array<String>) {
        val parser = argParserFactory.create(runner)
        parser.parse(args)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(arrayOf(BioRelatives::class.java), args)
}
