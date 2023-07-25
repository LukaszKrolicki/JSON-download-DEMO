package eu.pl.snk.senseibunny.websitedownloaddemo

data class ResponseData(val id: Int, val name: String, val profile_details: profile_details, val parents: List<String>, val parents_details: List<parent_details>)

data class profile_details(val status: String, val badge: String)

data class parent_details(val age: Int, val hobby:String)