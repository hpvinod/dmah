package com.accenture.aa.dmah.core.audit

/**
 * Enumeration contain constants for job status
 * 
 * @author payal.patel
 */
object JobStatus extends Enumeration {
        type JobStatus = Value
        val NOTSTARTED, INITIATED, RUNNING, COMPLETED, ERROR = Value
    }