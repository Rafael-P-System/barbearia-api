package barbearia.service;

import barbearia.entity.Cliente;
import barbearia.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente validarLogin(String email, String senha) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            // O getSenha() vem do @Data do Lombok lá na Entity Cliente
            if (cliente.getSenha().equals(senha)) {
                return cliente;
            }
        }
        return null;
    }
}