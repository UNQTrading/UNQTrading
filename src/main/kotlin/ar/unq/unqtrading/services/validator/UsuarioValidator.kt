package ar.unq.unqtrading.services.validator

import ar.unq.unqtrading.entities.Usuario
import ar.unq.unqtrading.repositories.UsuarioRepository
import ar.unq.unqtrading.services.exceptions.*
import ar.unq.unqtrading.utils.ObjectStructureUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UsuarioValidator {
    @Autowired
    lateinit var usuarioRepository: UsuarioRepository

    fun validate(usuario: Usuario) {
        ObjectStructureUtils.checkEmptyAttributes(usuario)
        if (usuarioRepository.findByDni(usuario.dni) != null) {
            throw UsuarioYaExistenteException("Ya hay un usuario registrado con el dni ${usuario.dni}")
        }
        if (usuarioRepository.findByCuil(usuario.cuil) != null) {
            throw UsuarioYaExistenteException("Ya hay un usuario registrado con el cuil ${usuario.cuil}")
        }
        if(usuarioRepository.findByEmail(usuario.email) != null ) {
            throw UsuarioYaExistenteException("Ya hay un usuario registrado con el email ${usuario.email}")
        }
        if(usuarioRepository.findByUsername(usuario.username) != null ) {
            throw UsuarioYaExistenteException("Ya hay un usuario registrado con el nombre de usuario ${usuario.username}")
        }
    }

    fun validateLogin(dni: Long, username: String, password: String, usuario: Usuario?) {
        validateDni(dni)
        validateUsername(username)
        validatePassword(password)
        if (usuario === null || !usuario.password.contentEquals(password)) {
            throw LoginFallidoException("Datos ingresados incorrectos")
        }
    }


    private fun validateDni(dni: Long) {
        if ("$dni".length !== 8) {
            throw DniInvalidoException("El dni debe tener 8 dígitos")
        }
    }

    private fun validateUsername(username: String) {
        if (username.isEmpty()) {
            throw UsernameInvalidoException("El nombre de usuario no puede estar vacío")
        }
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            throw PasswordInvalidaException("La contraseña no puede estar vacía")
        }
    }
}