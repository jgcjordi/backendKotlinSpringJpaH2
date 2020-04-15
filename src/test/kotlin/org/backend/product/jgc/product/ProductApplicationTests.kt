package org.backend.product.jgc.product

import com.fasterxml.jackson.databind.ObjectMapper
import org.backend.product.jgc.product.domain.Product
import org.backend.product.jgc.product.service.ProductService
import org.backend.product.jgc.product.service.ProviderService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*

@SpringBootTest //Funcionalidades especiale para los test
//@AutoConfigureMockMvc //Delega a spring la tarea de configurar "MocMvc"
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)//hace que los tests realicen alfabeticamente por el nombre del metodo
class ProductApplicationTests {

    //    @Autowired
//    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext//del framework spring

    private val mockMvc: MockMvc by lazy {//inicializacion perezosa, sera inicializada cuando se necesitada, bajo demanda "by".
        //configura el mockMvc para que cada vez que haga una peticion http la imprima en la consola.
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
    }

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var providerService: ProviderService

    private val defautlProvider by lazy{
        providerService.findAll().first()
    }


    private val productEndPoint = "/api/v1/products"

    @Test
    fun findAll() { //esto hace una peticion http y si devuelve el 200 pasa
        val productFromService = productService.findAll()

        val products: List<Product> = mockMvc.perform(MockMvcRequestBuilders.get(productEndPoint))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assertThat(productFromService, Matchers.`is`(Matchers.equalTo(products)))
    }

    @Test
    fun findById() {
        val productFromService = productService.findAll()
        assert(productFromService.isNotEmpty()) { "Should not be empty" }
        val product = productFromService.first()

        mockMvc.perform((MockMvcRequestBuilders.get(productEndPoint + "/" + product.name)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name", Matchers.`is`(product.name)))
    }

    @Test
    fun findByIdEmpty() {
        mockMvc.perform((MockMvcRequestBuilders.get(productEndPoint + "/" + UUID.randomUUID())))
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun saveSuccessfully() {
        val product = Product(name = "PineApple", price = 50.0, provider = defautlProvider)
        val productFromApi: Product = mockMvc.perform(MockMvcRequestBuilders.post(productEndPoint)
                .body(product, mapper))
                .andExpect(status().isCreated)
                .bodyTo(mapper)

        assert(productService.findAll().contains(productFromApi))//comprueba si es true
    }

    @Test
    fun saveCheckRules() {
        mockMvc.perform(MockMvcRequestBuilders.post(productEndPoint)
                .body(Product(name ="Pi", price = -50.00, provider = defautlProvider), mapper))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
    }

    @Test
    fun saveDuplicateEntity() {
        val productFromService = productService.findAll()
        assert(productFromService.isNotEmpty()) { "Should not be empty" }
        val product = productFromService.first()
        mockMvc.perform(MockMvcRequestBuilders.post(productEndPoint)
                .body(product, mapper))
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))
    }

    @Test
    fun saveEntityNotFound() {
        val productFromService = productService.findAll()
        assert(productFromService.isNotEmpty()) { "Should not be empty" }
        val product = productFromService.first().copy(name = "Entity test").apply {
            this.provider.id = 50
        }
        mockMvc.perform(MockMvcRequestBuilders.post(productEndPoint)
                .body(product, mapper))
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.title", Matchers.`is`("JpaObjectRetrievalFailureException")))
    }

    @Test
    fun updateSuccessfully() {
        val productFromService = productService.findAll()
        assert(productFromService.isNotEmpty()) { "Should not be empty" }
        val product = productFromService.first().copy(price = 44.20)//copia el producto cambiandole el precio
        val productFromApi: Product = mockMvc.perform(MockMvcRequestBuilders.put(productEndPoint)
                .body(product, mapper))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assertThat(productService.findById(product.name), Matchers.`is`(productFromApi))
    }

    @Test
    fun updateEntityNotFound() {
        val product = Product(name = UUID.randomUUID().toString(), price = 123.20, provider = defautlProvider)
        mockMvc.perform(MockMvcRequestBuilders.put(productEndPoint)
                .body(product, mapper))
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))
    }

    @Test
    fun deleteById() {
        val productFromService = productService.findAll()
        assert(productFromService.isNotEmpty()) { "Should not be empty" }
        val product = productFromService.last()
        val productFromApi: Product = mockMvc.perform(MockMvcRequestBuilders.delete(productEndPoint + "/" + product.name))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assertThat(product, Matchers.`is`(productFromApi))
        assert(!productService.findAll().contains(product))
    }

    @Test
    fun deleteByIdEntityNotFound() {
        mockMvc.perform(MockMvcRequestBuilders.delete(productEndPoint + "/" + UUID.randomUUID()))
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))
    }
}
