package com.example.service;

import com.example.model.Admin;
import com.example.model.Doctor;
import com.example.model.Patient;
import com.example.repository.AdminRepository;
import com.example.repository.DoctorRepository;
import com.example.repository.PatientRepository;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public TokenService(AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // Génère un JWT
    public String generateToken(String identifier) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 604800000L); // 7 jours en ms

        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrait l'identifiant (email ou username) du token
    public String extractIdentifier(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valide un token selon le type d'utilisateur
    public boolean validateToken(String token, String userType) {
        try {
            String identifier = extractIdentifier(token);
            switch (userType.toLowerCase()) {
                case "admin":
                    Optional<Admin> admin = adminRepository.findByUsername(identifier);
                    return admin.isPresent();
                case "doctor":
                    Optional<Doctor> doctor = doctorRepository.findByEmail(identifier);
                    return doctor.isPresent();
                case "patient":
                    Optional<Patient> patient = patientRepository.findByEmail(identifier);
                    return patient.isPresent();
                default:
                    return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token invalide ou expiré
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
