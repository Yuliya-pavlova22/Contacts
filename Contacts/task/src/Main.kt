package contacts
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.lang.System.exit
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess


val moshi = Moshi.Builder()
    .add(
        PolymorphicJsonAdapterFactory.of(BaseContact::class.java, "_type")
        .withSubtype(Contact::class.java, TypeContact.contact.name)
        .withSubtype(Organization::class.java, TypeContact.organization.name))
    .add(KotlinJsonAdapterFactory())
    .build()

enum class TypeContact {
    @Json(name = "contact")
    contact,
    @Json(name = "organization")
    organization
}



sealed class BaseContact(@Json(name = "_type") open var type: TypeContact) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val lokalDate = LocalDateTime.now().format(formatter)
    var dateCreation = lokalDate.toString()
    var dateLastEdit = lokalDate.toString()
   // open val type = ""
    var telNum: String = ""
//        set(value) {
//            var regex = "(\\+?(([A-Za-z0-9]+)|(\\([A-Za-z0-9]+\\))|(\\([A-Za-z0-9]+\\)[\\s-][A-Za-z0-9]{2,})|([A-Za-z0-9]+[\\s-]\\([A-Za-z0-9]{2,}\\))|([A-Za-z0-9]+[\\s-][A-Za-z0-9]{2,})))?(([A-Za-z0-9]{2,}([\\s-][A-Za-z0-9]{2,})*)|([\\s-][A-Za-z0-9]{2,})+)?".toRegex()
//            if (regex.matches(value))
//            // if (isValidPhone(value))
//                field = value
//            else {
//                println("Wrong number format!")
//                field = "[no number]"
//            }
//        }

    open fun fill() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val lokalDate = LocalDateTime.now().format(formatter)
        dateCreation = lokalDate.toString()
        dateLastEdit = lokalDate.toString()
        print("Enter the number: ")
        var telTxt = readln()
        var regex = "(\\+?(([A-Za-z0-9]+)|(\\([A-Za-z0-9]+\\))|(\\([A-Za-z0-9]+\\)[\\s-][A-Za-z0-9]{2,})|([A-Za-z0-9]+[\\s-]\\([A-Za-z0-9]{2,}\\))|([A-Za-z0-9]+[\\s-][A-Za-z0-9]{2,})))?(([A-Za-z0-9]{2,}([\\s-][A-Za-z0-9]{2,})*)|([\\s-][A-Za-z0-9]{2,})+)?".toRegex()
            if (regex.matches(telTxt))
            // if (isValidPhone(value))
                telNum = telTxt
            else {
                println("Wrong number format!")
                telNum = "[no number]"
            }
    }

    open fun ShowItem() = "Number: $telNum\n"+
            "Time created: $dateCreation\n" +
            "Time last edit: $dateLastEdit"

    open fun Edit() {}

    open fun toJson(): String {
        return "implement"
    }
}

class Contact(): BaseContact(TypeContact.contact) {
    @Json(name = "name")
    var name: String = ""
    var surName: String = ""
    var _type = TypeContact.contact

    var birth = ""
        set(value) {
            //println("asdasd $value")
            field =
            try {
                LocalDate.parse( value )
                value
            } catch (e: Exception) {
                //println("Bad birth date!")
                "[no data]"
            }
        }

    private var gender = ""
        set(value) {
            field = if (value == "M" || value == "F" || value == "m" || value == "f" )
                value
            else {
                //println("Bad gender!")
                "[no data]"
            }
        }

    override fun toString(): String {
        return "$name $surName"
    }

    override fun fill() {
        println("Enter the name of the person: ")
        name = readln()
        println("Enter the surname of the person: ")
        surName = readln()

        println("Enter the birth date: ")
        var lbirth = readln()
        birth = lbirth

        println("Enter the gender (M, F): ")
        gender = readln()

        super.fill()

        File("log.txt").appendText("name=$name surName=$surName lbirth=$lbirth gender=$gender\r\n")

//        val newContack = Contact()
//       // newContack.telNum = super.telNum
//        newContack.name = name
//        newContack.gender = gender
//        newContack.birth = birth
//        newContack.surName = surName
//        newContack.type = type
        println("The record added.")
        println()
    }

    override fun ShowItem() = "Name: $name\n" +
            "Surname: $surName\n" +
            "Birth date: $birth\n" +
            "Gender: $gender\n" +
            super.ShowItem()

    override fun Edit() {
        print("Select a field (name, surname, birth, gender, number): ")
        var inputParam = readln()
        val lokalDate = LocalDate.now()
        when (inputParam) {
            "name" -> {
                print("Enter name: ")
                name = readln()
                dateLastEdit = lokalDate.toString()
                println("Saved")
                println(this.ShowItem())
            }
            "surname" -> {
                print("Enter surname: ")
                surName = readln()
                dateLastEdit = lokalDate.toString()
                println("Saved")
                println(this.ShowItem())
            }
            "birth" -> {
                print("Enter birth: ")
                birth = readln()
                dateLastEdit = lokalDate.toString()
                println("Saved")
                println(this.ShowItem())
            }
            "gender" -> {
                print("Enter gender: ")
                gender = readln()
                dateLastEdit = lokalDate.toString()
                println("Saved")
                println(this.ShowItem())
            }
            "number" -> {
                print("Enter number:")
                telNum = readln()
                dateLastEdit = lokalDate.toString()
                println("Saved")
                println(this.ShowItem())
            }
            "menu" -> {
                main()
            }
            "exit" -> {
                exitProcess(0)
            }
            else -> {
                println("error")
                Edit()
            }
        }


    }

    override fun toJson(): String {
        val bookListAdapter = moshi.adapter(Contact::class.java)
        return bookListAdapter.toJson(this)
    }

}

open class Organization(): BaseContact(TypeContact.organization) {
    @Json(name = "name")
    var name =  ""
    var address = ""
    var _type = TypeContact.organization

    override fun toString(): String {
        return "$name"
    }

    override fun fill() {
        print("Enter the organization name: ")
        name = readln()
        print("Enter the address: ")
        address = readln()

        File("log.txt").appendText("name=$name surName=$address\r\n")

        super.fill()
//        val newContack = Organization()
//       // newContack.telNum = super.telNum
//        newContack.name = name
//        newContack.address = address
//        newContack.type = type
        println("The record added.")
        println()
       // println("Saved")
        println(this.ShowItem())

    }

    override fun ShowItem(): String {
        var s = "Organization name: $name\n" +
                "Address: $address\n" +
                super.ShowItem()

        File("log.txt").appendText("org ShowItem=$s\r\n")

        return s
    }

    override fun toJson(): String {
        val bookListAdapter = moshi.adapter(Organization::class.java)
        return bookListAdapter.toJson(this)
    }

    override  fun Edit(){
        println("Select a field (name, address, number): ")
        var inputParam = readln()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val lokalDate = LocalDateTime.now().format(formatter)
        when (inputParam) {
            "name" -> {
                print("Enter name: ")
                name = readln()
                dateLastEdit = lokalDate.toString()
            }
            "address" -> {
                print("Enter address: ")
                address = readln()
                dateLastEdit = lokalDate.toString()
            }
            "number" -> {
                print("Enter number: ")
                telNum = readln()
                dateLastEdit = lokalDate.toString()
            }
            "menu" -> {
                main()
            }
            "exit" -> {
                exitProcess(0)
            }
            else -> {
                println("error")
                Edit()
            }
        }
        println("Saved")
        println(this.ShowItem())


    }

}



class Book(){
    var contactBook: MutableMap<Int, BaseContact> = mutableMapOf()
    var fileBook: File = File("")
    var Size: Int = 0
        get() = contactBook.size

    fun add(isPersonal: Boolean, fileBook: File){
        var isContact = isPersonal
        var record: BaseContact
        if (isContact) {
            record = Contact()
        }
        else
            record = Organization()

        File("log.txt").appendText("isPersonal=$isPersonal\r\n")

        record.fill();
        var newBook = contactBook.toMutableMap()
        contactBook.clear()
        contactBook[1] = record
        var key  = 2
        for (element in newBook) {
            contactBook.put(key, element.value)
            key++

        }



//
//        var numKey = contactBook.size + 1
//        contactBook[numKey] = record

        updateFile()
    }

    fun list() {
        contactBook.forEach {
            println("${it.key}. ${it.value.toString()}")
        }
        listAction()
    }

        fun listAction(){
            println()
            print("[list] Enter action ([number], back): ")
            var action = readln()
            if (isNumeric(action)) {
                println(contactBook[action.toInt()]?.ShowItem())
                contactBook[action.toInt()]?.let { recordEction(it) }
            } else {
                when (action) {
                    "back"-> {
                        return
                    }
                    else -> {
                        println("commands not found")
                    }
                }
        }
    }

    fun showItem(item: Int) {
        val el = contactBook[item]
        if (el != null) {
            println(el.ShowItem())
        }
    }

//  open fun edit(fileBook: File) {
//        println("Select a record:")
//        var inputKey = readln().toInt()
//      val el = contactBook[inputKey]
//      if (el != null) {
//          el.Edit()
//          updateFile()
//
//          println("The record updated!")
//      }
//    }

    fun updateFile() {
        var jsonList = contactBook.map { it.value.toJson() }

        var json = jsonList.joinToString(separator = ",") { it }
        json = "[$json]"
        fileBook.writeText(json)
    }


    fun delete(inFile: File, remKey : Int) {
      //  println("Select a record:")
      //  var remKey = readln().toInt()
        contactBook.remove(remKey)

        var newBook = mutableMapOf<Int, BaseContact>()

        for (kv in contactBook){
            var id = kv.key
            if (kv.key > remKey)
                id -= 1

            newBook.put(id, kv.value)
        }

        contactBook = newBook
        updateFile()

        println("The record removed!")
    }

    fun fromJson(str: String){
        val bookListAdapter =
            moshi.adapter<List<BaseContact>>(Types.newParameterizedType(List::class.java, BaseContact::class.java))
        val bookList = bookListAdapter.fromJson(str.trimIndent())
        var i = 1
            for (el in bookList!!) {
                    var record = el!!
                contactBook.put(i,record)
               // println(record)
                i++
            }
    }

    fun search(){
        var result = mutableListOf<BaseContact>()
            print("Enter search query: ")
        var str = readln()
        File("log.txt").appendText("search=$str\r\n")

        var finalStr = ""
            val strToFind = str.lowercase()
            contactBook.forEach {
                var json = it.value.toJson().lowercase()
                 //println(json)

                if (json.indexOf(strToFind) != -1) {
                    result.add(it.value)
                }
            }
        var countRezult = result.size

        if (countRezult > 1) {
            println("Found $countRezult results:")
        }
        else {
            println("Found $countRezult result:")
        }

            for (i in 0..result.lastIndex) {
                var el = result[i]
                var str = ""
                if (el is Contact) {
                    str = "${(el as Contact).name} ${(el as Contact).surName}"
                } else {
                    str = (el as Organization).name
                }
                println("${i + 1}. ${str}")
            }
        searchAction(result)
    }

    fun searchAction(res : MutableList<BaseContact>) {
        println()
        print("[search] Enter action ([number], back, again): ")
        var action = readln()
        if (isNumeric(action)) {
            println(res[action.toInt() - 1].ShowItem())
            recordEction(res[action.toInt() - 1])

        } else {
            when (action) {
                "back"-> {
                    return
                }
                "again" -> {
                    search()
                }
            }
        }
    }

    fun recordEction(element: BaseContact) {
        println()
        print("[record] Enter action (edit, delete, menu): ")
        var action = readln()
        when (action) {
            "edit" -> {
                element.Edit()
                updateFile()
                recordEction(element)
            }
            "delete" -> {
                var key = faindKey(element)+1
                println(key)
                delete(fileBook,key)
                updateFile()
            }
            "menu" -> {
                return
            }
        }

    }

    fun faindKey(element: BaseContact): Int {
        var num = 0
        for (i in contactBook) {
            if (i == element)  num = i.key
        }
        return  num
    }


}



fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}


fun main(args: Array<String> = arrayOf("open", "Contacts.db")) {
    var theEnd = false

    var inFile : File

    if (args.size == 2) {
        var inFiletxt = args[1]
        inFile = File(inFiletxt)
    } else {
        inFile = File("Contacts.db")
    }
    var book = Book()
    book.fileBook = inFile
    // var data = inFile.readText()
    // var outFile = File(outFiletxt)

    if (inFile.exists()) {
        var str = inFile.readText()
        if (str != "")
        book.fromJson(str)
    } else inFile.createNewFile()


    while (!theEnd) {
        //println()
        print("[menu] Enter action (add, list, search, count, exit): ")

        val action = readln()!!

        File("log.txt").appendText(action + "\r\n")

        when (action) {
            "add" -> {
                print("Enter the type (person, organization): ")
                var isPersonal = (readln() == "person")

                book.add(isPersonal, inFile)

            }
            "search" -> {
                book.search()
            }
            "count" -> {
                println("The Phone Book has ${book.Size} records.") }

            "list" -> {
                if (book.Size == 0) {
                    println("No records in book!")
                    continue
                }
                book.list()


            }
            "exit" -> {
                File("Contacts.db").delete()
                exitProcess(0)
            }
            else -> {
                println("command not found")
                continue
            }
        }

        println()
    }
}





