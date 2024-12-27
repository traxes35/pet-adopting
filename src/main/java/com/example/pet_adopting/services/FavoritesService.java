    package com.example.pet_adopting.services;

    import com.example.pet_adopting.entities.Favorites;
    import com.example.pet_adopting.entities.Pet;
    import com.example.pet_adopting.entities.User;
    import com.example.pet_adopting.repos.FavoritesRepository;
    import com.example.pet_adopting.repos.UserRepository;
    import com.example.pet_adopting.requests.FavoritesRequest;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    public class FavoritesService {
        private final UserService userService;
        private final PetService petService;
        private final FavoritesRepository favoritesRepository;
        private final UserRepository userRepository;

        public FavoritesService(UserService userService, PetService petService, FavoritesRepository favoritesRepository, UserRepository userRepository) {
            this.userService = userService;
            this.petService = petService;
            this.favoritesRepository = favoritesRepository;
            this.userRepository = userRepository;
        }

        public List<Pet> getAllFavorites(Long userId) {
            List<Favorites> favorites = favoritesRepository.findByUserId(userId);
            List<Pet> pets = new ArrayList<>();
            for (Favorites favorite : favorites) {
                pets.add(favorite.getPet());
            }
            return pets;
        }

        public Pet getFavoritesPetById(Long favoritesId) {
            return favoritesRepository.findById(favoritesId)
                    .map(Favorites::getPet)
                    .orElse(null); // Eğer favori bulunamazsa null döner
        }

        public Favorites addFavorites(FavoritesRequest favorites) {
            User user =userService.getOneUserbyId(favorites.getUserId());
            Pet pet = petService.getPetById(favorites.getPetId());
            if (user !=null &&pet!=null) {
                Favorites favoritestoSave = new Favorites();
                favoritestoSave.setUser(user);
                favoritestoSave.setPet(pet);
                favoritesRepository.save(favoritestoSave);
            }
            return null;
        }

        public void deleteFavorites(Long favoritesId) {
        if (favoritesRepository.existsById(favoritesId)) {
        favoritesRepository.deleteById(favoritesId);
        }
    }

        public List<Pet> getFavoritesForCurrentUser() {
            // Login olan kullanıcının bilgilerini al
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
              return null;
             }

            // Kullanıcıyı doğrula
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);

            // Kullanıcı ID'sini kullanarak favorileri al
            List<Favorites> favorites = favoritesRepository.findByUserId(user.getId());
            return favorites.stream()
                    .map(Favorites::getPet)
                    .collect(Collectors.toList());
        }
    }
