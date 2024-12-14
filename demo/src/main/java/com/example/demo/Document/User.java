package com.example.demo.Document;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
    public class User implements UserDetails {
        @Id
        private String id;

        private String username;

        @JsonIgnore
        private String password;
        private String email;
        private String phone;
        private LocalDate dob;
        private long totalFiles;
        private long dataMB;
        private ArrayList<String> listOfFiles;
        private boolean isEnabled;

        private String  api_key;


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(); // Add roles if needed
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        public void addToList(String s){
            this.listOfFiles.add(s);
        }

}
