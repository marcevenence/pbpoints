package com.pbpoints.service;

import static java.nio.file.StandardOpenOption.*;
import static java.nio.file.StandardOpenOption.DELETE_ON_CLOSE;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.html2pdf.HtmlConverter;
import com.pbpoints.domain.*;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.repository.*;
import com.pbpoints.service.dto.EventDTO;
import com.pbpoints.service.dto.xml.GameResultDTO;
import com.pbpoints.service.mapper.EventMapper;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    private final EventCategoryRepository eventCategoryRepository;

    private final UserExtraRepository userExtraRepository;

    private final CategoryRepository categoryRepository;

    private final com.pbpoints.service.GameService gameService;

    private final GameRepository gameRepository;

    private final TeamPointRepository teamPointRepository;

    private final TeamDetailPointRepository teamPointDetailRepository;

    private final RosterRepository rosterRepository;

    private final SponsorRepository sponsorRepository;

    private final EventMapper eventMapper;

    private final PlayerPointService playerPointService;

    public EventService(
        EventRepository eventRepository,
        EventCategoryRepository eventCategoryRepository,
        UserExtraRepository userExtraRepository,
        CategoryRepository categoryRepository,
        GameService gameService,
        TeamPointRepository teamPointRepository,
        TeamDetailPointRepository teamPointDetailRepository,
        GameRepository gameRepository,
        RosterRepository rosterRepository,
        SponsorRepository sponsorRepository,
        EventMapper eventMapper,
        PlayerPointService playerPointService
    ) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.teamPointRepository = teamPointRepository;
        this.teamPointDetailRepository = teamPointDetailRepository;
        this.userExtraRepository = userExtraRepository;
        this.categoryRepository = categoryRepository;
        this.rosterRepository = rosterRepository;
        this.sponsorRepository = sponsorRepository;
        this.eventMapper = eventMapper;
        this.playerPointService = playerPointService;
    }

    /**
     * Save a event.
     *
     * @param eventDTO the entity to save.
     * @return the persisted entity.
     */
    public EventDTO save(EventDTO eventDTO) {
        log.debug("Request to save Event : {}", eventDTO);
        Event event = eventMapper.toEntity(eventDTO);
        event = eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    /**
     * Partially update a event.
     *
     * @param eventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventDTO> partialUpdate(EventDTO eventDTO) {
        log.debug("Request to partially update Event : {}", eventDTO);

        return eventRepository
            .findById(eventDTO.getId())
            .map(
                existingEvent -> {
                    eventMapper.partialUpdate(existingEvent, eventDTO);
                    return existingEvent;
                }
            )
            .map(eventRepository::save)
            .map(eventMapper::toDto);
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        return eventRepository.findAll(pageable).map(eventMapper::toDto);
    }

    /**
     * Get one event by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventDTO> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id).map(eventMapper::toDto);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }

    /**
     * A partir de todos los los equipos que van a participar en un
     * evento-categoria, se genera el fixture para generar los games
     *
     * @param event evento para generar el xml
     * @return XML con el evento
     * @throws IOException en el caso que no se pueda generar el xml
     */
    public File generarXML(Event event) throws IOException {
        log.info("*** Generando XML para el evento {}", event);

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            log.info("*** Creando element PBPOINTS", event);

            Element root = document.createElement("PBPOINTS");
            document.appendChild(root);

            log.info("*** Creando element EVENT_ID", event);

            Element eventId = document.createElement("EVENT_ID");
            eventId.appendChild(document.createTextNode(event.getId().toString()));
            root.appendChild(eventId);

            log.info("*** Creando element OWNER_ID", event);

            log.info("*** Creando element OWNER_ID: {}", event.getTournament());

            //CORREGIR NO LEVANTA, TOURNAMENT NO TIENE OWNER?
            Element ownerId = document.createElement("OWNER_ID");
            ownerId.appendChild(document.createTextNode(event.getTournament().getOwner().getId().toString()));
            root.appendChild(ownerId);

            log.info("*** Creando element SETUP", event);

            Element setup = document.createElement("SETUP");
            root.appendChild(setup);

            List<EventCategory> eventCategories = eventCategoryRepository.findByEvent(event);
            log.info("*** Recorriendo Categorias ***");
            for (EventCategory eventCategory : eventCategories) {
                log.info("*** Recorriendo Categoria {}", eventCategory);
                Element categorys = document.createElement("CATEGORY");
                setup.appendChild(categorys);

                Element name = document.createElement("NAME");
                name.appendChild(document.createTextNode(eventCategory.getCategory().getName()));
                categorys.appendChild(name);

                Element timeType = document.createElement("TIME_TYPE");
                timeType.appendChild(document.createTextNode(eventCategory.getCategory().getGameTimeType().name()));
                categorys.appendChild(timeType);

                Element time = document.createElement("TIME");
                time.appendChild(document.createTextNode(eventCategory.getCategory().getGameTime().toString()));
                categorys.appendChild(time);

                Element waitType = document.createElement("WAIT_TYPE");
                waitType.appendChild(document.createTextNode(eventCategory.getCategory().getStopTimeType().name()));
                categorys.appendChild(waitType);

                Element wait = document.createElement("WAIT");
                wait.appendChild(document.createTextNode(eventCategory.getCategory().getStopTime().toString()));
                categorys.appendChild(wait);

                Element waitSpType = document.createElement("WAIT_SP_TYPE");
                waitSpType.appendChild(document.createTextNode("MINUTES"));
                categorys.appendChild(waitSpType);

                Element waitSp = document.createElement("WAIT_SP");
                waitSp.appendChild(document.createTextNode("1"));
                categorys.appendChild(waitSp);

                Element points = document.createElement("POINTS");
                points.appendChild(document.createTextNode(eventCategory.getCategory().getTotalPoints().toString()));
                categorys.appendChild(points);

                Element dif = document.createElement("DIF");
                dif.appendChild(document.createTextNode(eventCategory.getCategory().getDifPoints().toString()));
                categorys.appendChild(dif);
            }
            Element fixture = document.createElement("FIXTURE");
            root.appendChild(fixture);

            for (EventCategory eventCategory : eventCategories) {
                Element gamesxml = document.createElement("GAMES");
                fixture.appendChild(gamesxml);

                List<Game> games = gameRepository.findByEventCategory(eventCategory);
                if (!games.isEmpty()) {
                    log.info("*** Recorriendo Games ***");
                    for (Game gameloop : games) {
                        Element game = document.createElement("GAME");
                        gamesxml.appendChild(game);

                        log.info("*** Recorriendo Game {}", gameloop);

                        Element category = document.createElement("CATEGORY_ID");
                        category.appendChild(document.createTextNode(eventCategory.getCategory().getId().toString()));
                        game.appendChild(category);

                        Element gameid = document.createElement("ID");
                        gameid.appendChild(document.createTextNode(gameloop.getId().toString()));
                        game.appendChild(gameid);

                        Element spid = document.createElement("SD_ID");
                        spid.appendChild(document.createTextNode(gameloop.getSplitDeckNum().toString()));
                        game.appendChild(spid);

                        Element clasif = document.createElement("CLASIF");
                        clasif.appendChild(document.createTextNode("1"));
                        game.appendChild(clasif);

                        Element teamIda = document.createElement("TEAM_ID_A");
                        teamIda.appendChild(document.createTextNode(gameloop.getTeamA().getId().toString()));
                        game.appendChild(teamIda);

                        Element teama = document.createElement("TEAM_A");
                        teama.appendChild(document.createTextNode(gameloop.getTeamA().getName()));
                        game.appendChild(teama);

                        Element pointsa = document.createElement("POINTS_A");
                        int pointsA = gameloop.getPointsA() == null ? 0 : gameloop.getPointsA();
                        pointsa.appendChild(document.createTextNode(Integer.toString(pointsA)));
                        game.appendChild(pointsa);

                        Element teamIdb = document.createElement("TEAM_ID_B");
                        teamIdb.appendChild(document.createTextNode(gameloop.getTeamB().getId().toString()));
                        game.appendChild(teamIdb);

                        Element teamb = document.createElement("TEAM_B");
                        teamb.appendChild(document.createTextNode(gameloop.getTeamB().getName()));
                        game.appendChild(teamb);

                        Element pointsb = document.createElement("POINTS_B");
                        int pointsB = gameloop.getPointsB() == null ? 0 : gameloop.getPointsB();
                        pointsb.appendChild(document.createTextNode(Integer.toString(pointsB)));
                        game.appendChild(pointsb);
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            // nombre del fichero
            String path = "PBPOINTS_" + event.getName().replace(" ", "").toUpperCase() + ".pbp";
            // seteo el directorio de acuerdo al sistema operativo en donde este instalado
            String directory = null;
            if (SystemUtils.IS_OS_UNIX) {
                directory = SystemUtils.USER_HOME.concat("/PBPoints/");
            } else if (SystemUtils.IS_OS_WINDOWS) {
                directory = "C:\\PBPoints\\";
            }
            // creo el directorio
            File file = new File(directory);
            if (!file.exists()) {
                file.mkdir();
            }
            // creo el fichero (si existia, lo borro para tener uno nuevo)
            file = new File(directory.concat(path));
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileWriter writer = new FileWriter(new File(directory.concat(path)));
            StreamResult result = new StreamResult(writer);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(domSource, result);
            log.info("*** Fichero Generado: --> {}", directory.concat(path));
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            transformer.transform(domSource, new StreamResult(oStream));

            // devuelvo el xml para descargarlo
            return file;
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
        return null;
    }

    public boolean hasCategories(Event event) {
        List<EventCategory> eventCategories = eventCategoryRepository.findByEvent(event);
        log.debug("Event Categories: {}" + eventCategories);
        if (eventCategories.isEmpty()) return false; else return true;
    }

    public boolean hasGames(Event event) {
        int qty = 0;
        List<EventCategory> eventCategories = eventCategoryRepository.findByEvent(event);
        for (EventCategory eventCategory : eventCategories) {
            List<Game> games = gameRepository.findByEventCategory(eventCategory);
            if (!games.isEmpty()) {
                qty++;
            }
        }
        if (qty == 0) return false; else return true;
    }

    public Boolean submitXML(MultipartFile file) {
        XmlMapper xmlMapper = new XmlMapper();
        log.info("*** Inicio parseo de fichero con resultados de EventCategory ***");
        try {
            log.info("Fichero: {}", file.getOriginalFilename());
            GameResultDTO gameResultDTO = xmlMapper.readValue(file.getBytes(), GameResultDTO.class);
            log.debug(gameResultDTO.toString());
            // Validaciones de entidades
            /*UserExtra userExtra = userExtraRepository
                .findById(gameResultDTO.getOwner_id())
                .orElseThrow(() -> new IllegalArgumentException("No existe un Usuario con el ID " + gameResultDTO.getOwner_id()));*/
            Event event = eventRepository
                .findById(gameResultDTO.getEvent_id())
                .orElseThrow(() -> new IllegalArgumentException("No existe un evento con ID: " + gameResultDTO.getEvent_id()));
            Category category = categoryRepository
                .findById(gameResultDTO.getFixtureDTO().getCategoryDTO().getId())
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "No existe una Categoria con el nombre: " + gameResultDTO.getFixtureDTO().getCategoryDTO().getName()
                        )
                );
            EventCategory eventCategory = eventCategoryRepository
                .findByEventAndCategory(event, category)
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "No existe la combinacion de Evento-Categoria " + event.toString() + " - " + category.toString()
                        )
                );
            /*if (!event.getTournament().getOwner().equals(userExtra.getUser())) {
                throw new IllegalArgumentException(("El usuario " + userExtra.getUser().getLogin() + " no es el " + "owner del torneo"));
            }*/
            //Cargo los Games
            List<Game> games = gameResultDTO
                .getFixtureDTO()
                .getCategoryDTO()
                .getGames()
                .stream()
                .map(gameService::findByXML)
                .collect(Collectors.toList());
            gameRepository.saveAll(games);

            log.info(gameResultDTO.getPositions().toString());

            //Cargo los Team Points
            List<TeamPoint> teamPoints = gameResultDTO.getPositions().stream().map(gameService::findPosByXML).collect(Collectors.toList());
            for (TeamPoint teamPoint : teamPoints) {
                if (teamPoint.getTournament() == null) teamPoint.setTournament(event.getTournament());
            }
            teamPointRepository.saveAll(teamPoints);

            //Cargo los Team Detail Points
            List<TeamDetailPoint> teamDetailPoints = gameResultDTO
                .getPositions()
                .stream()
                .map(gameService::findPosDetByXML)
                .collect(Collectors.toList());
            for (TeamDetailPoint teamPointDetail : teamDetailPoints) {
                teamPointDetail.setEventCategory(eventCategory);
            }
            teamPointDetailRepository.saveAll(teamDetailPoints);

            playerPointService.distPoints(gameResultDTO.getPositions(), event);

            // parseo el dto a mi modelo de datos
            log.info("** Parseo terminado");

            log.debug("** Games actualizados **");
            log.info("*** Fin de proceso de carga de puntajes ***");
        } catch (IOException e) {
            log.error("Error al parsear el fichero: {}", file.getName());
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Integer getlengthFixture(Event event) {
        Integer quantity = 0;
        List<EventCategory> eventCategories = eventCategoryRepository.findByEvent(event);
        for (EventCategory eventCategory : eventCategories) {
            List<Game> games = gameRepository.findByEventCategory(eventCategory);
            quantity = quantity + games.size();
            quantity = quantity + 1;
        }
        return quantity;
    }

    public String getSponsorslogo(Tournament tournament) {
        String result = "";
        List<Sponsor> sponsors = sponsorRepository.findByTournament(tournament);
        for (Sponsor sponsor : sponsors) {
            result =
                result +
                "<img src='data:" +
                sponsor.getLogoContentType() +
                ";base64," +
                Base64.getEncoder().encodeToString(sponsor.getLogo()) +
                "' style=\"width: 75px; height: 75px\" alt=\"sponsor image\" />\n";
        }
        return result;
    }

    public void generateScore(Event event) throws IOException, URISyntaxException {
        log.debug("*** Generando PDF Score ***");
    }

    public void generateTablePoint(Event event) throws IOException, URISyntaxException {
        log.debug("*** Generando PDF Table Point ***");
        Path tempFile = Files.createTempFile(null, ".html");
        Path tempPDFFile = Files.createTempFile(null, ".pdf");
        Path tempFileCss = Files.createTempFile(null, ".scs");
        System.out.println(tempPDFFile);
        System.out.println(tempFile);
        System.out.println(tempFileCss);

        String htmlString = Files.readString(Paths.get(ClassLoader.getSystemResource("templates/pdf/tablePoint.html").toURI()));
        String title = event.getTournament().getName() + " - " + event.getName();
        String tournamentName = event.getTournament().getName();
        String eventName = event.getName();
        String mainTitle =
            "<div id=\\\"wrapper\\\">" +
            "  \n" +
            "               <h1 style=\"text-align: center\">" +
            tournamentName +
            "</h1>\n" +
            "               <h2 style=\"text-align: center\">" +
            eventName +
            "</h2>\n";

        String tableHeader =
            "  <table id=\"keywords\" style =\"margin-left: auto; margin-right: auto\">\n" +
            "    <thead>\n" +
            "      <tr>\n" +
            "        <th width=\"14%\"></th>\n" +
            "        <th width=\"30%\"></th>\n" +
            "        <th width=\"5%\"></th>\n" +
            "        <th width=\"2%\"></th>\n" +
            "        <th width=\"5%\"></th>\n" +
            "        <th width=\"30%\"></th>\n" +
            "        <th width=\"14%\"></th>\n" +
            "      </tr>\n" +
            "    </thead>\n" +
            "    <tbody>";

        String body = "";

        String close = "    </tbody>\n" + "  </table>\n" + " </div>";

        htmlString = htmlString.replace("$title", title);
        htmlString = htmlString.replace("$scss", tempFileCss.toString());
        htmlString = htmlString.replace("$mainTitle", mainTitle);
        htmlString = htmlString.replace("$tableHeader", tableHeader);
        htmlString = htmlString.replace("$body", body);
        htmlString = htmlString.replace("$close", close);

        //        Files.copy(Paths.get(ClassLoader.getSystemResource("templates/pdf/event.css").toURI()), tempFileCss, StandardCopyOption.REPLACE_EXISTING);
        Files.write(tempFile, htmlString.getBytes(StandardCharsets.UTF_8));
        HtmlConverter.convertToPdf(new FileInputStream(tempFile.toString()), new FileOutputStream(tempPDFFile.toString()));
    }

    public File generatePdf(Event event) throws IOException, URISyntaxException {
        log.debug("*** Generando PDF ***");

        // write a tempFile
        Path tempFile = Files.createTempFile(null, ".html");
        Path tempPDFFile = Files.createTempFile(null, ".pdf");
        Path tempFileCss = Files.createTempFile(null, ".scs");
        System.out.println(tempPDFFile);
        System.out.println(tempFile);
        System.out.println(tempFileCss);

        String htmlString = Files.readString(Paths.get(ClassLoader.getSystemResource("templates/pdf/event.html").toURI()));
        String title = event.getTournament().getName() + " - " + event.getName();
        String tournamentName = event.getTournament().getName();
        String eventName = event.getName();

        String mainTitle =
            "<div id=\\\"wrapper\\\">" +
            "  \n" +
            "               <h1 style=\"text-align: center\">" +
            tournamentName +
            "</h1>\n" +
            "               <h2 style=\"text-align: center\">" +
            eventName +
            "</h2>\n";

        String tableHeader =
            "  <table id=\"keywords\" style =\"margin-left: auto; margin-right: auto\">\n" +
            "    <thead>\n" +
            "      <tr>\n" +
            "        <th width=\"14%\"></th>\n" +
            "        <th width=\"30%\"></th>\n" +
            "        <th width=\"5%\"></th>\n" +
            "        <th width=\"2%\"></th>\n" +
            "        <th width=\"5%\"></th>\n" +
            "        <th width=\"30%\"></th>\n" +
            "        <th width=\"14%\"></th>\n" +
            "      </tr>\n" +
            "    </thead>\n" +
            "    <tbody>";

        String body1 = "";

        String sponsors = getSponsorslogo(event.getTournament());
        Integer length = getlengthFixture(event);
        length = 1000;
        Integer cont = 0;
        List<EventCategory> eventCategories = eventCategoryRepository.findByEvent(event);
        for (EventCategory eventCategory : eventCategories) {
            cont++;
            if (cont == 1) {
                body1 =
                    body1 +
                    "      <tr>\n" +
                    "        <td style=\"writing-mode: vertical-rl; \" rowspan=\"" +
                    length +
                    "\">" +
                    sponsors +
                    "</td>\n" +
                    "        <td style=\"text-align: center\" colspan=\"5\"><h3>" +
                    eventCategory.getCategory().getName() +
                    "</h3></td>\n" +
                    "        <td style=\"writing-mode: vertical-rl; \" rowspan=\"" +
                    length +
                    "\">" +
                    sponsors +
                    "</td>\n" +
                    "      </tr>\n";
            } else {
                body1 =
                    body1 +
                    "      <tr>\n" +
                    "        <td style=\"text-align: center\" colspan=\"5\"><h3>" +
                    eventCategory.getCategory().getName() +
                    "</h3></td>\n" +
                    "      </tr>\n";
            }
            List<Game> games = gameRepository.findByEventCategoryAndClasif(eventCategory, "1");
            for (Game game : games) {
                int pointsA = game.getPointsA() == null ? 0 : game.getPointsA();
                int pointsB = game.getPointsB() == null ? 0 : game.getPointsB();
                body1 =
                    body1 +
                    "      <tr>\n" +
                    "        <td>" +
                    game.getTeamA().getName() +
                    "</td>\n" +
                    "        <td>" +
                    Integer.toString(pointsA) +
                    "</td>\n" +
                    "        <td>-</td>\n" +
                    "        <td>" +
                    Integer.toString(pointsA) +
                    "</td>\n" +
                    "        <td>" +
                    game.getTeamB().getName() +
                    "</td>\n" +
                    "      </tr>\n";
            }
        }
        if (event.getStatus() == Status.DONE) {
            for (EventCategory eventCategory : eventCategories) {
                body1 =
                    body1 +
                    "      <tr>\n" +
                    "        <td style=\"text-align: center\" colspan=\"5\"><h3>" +
                    eventCategory.getCategory().getName() +
                    " - Semifinales" +
                    "</h3></td>\n" +
                    "      </tr>\n";
                List<Game> games = gameRepository.findByEventCategoryAndClasif(eventCategory, "Semifinal");
                for (Game game : games) {
                    body1 =
                        body1 +
                        "      <tr>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getTeamA().getName() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getPointsA().toString() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">-</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getPointsB().toString() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getTeamB().getName() +
                        "</td>\n" +
                        "      </tr>\n";
                }
            }
            log.debug("HH");
            for (EventCategory eventCategory : eventCategories) {
                body1 =
                    body1 +
                    "      <tr>\n" +
                    "        <td style=\"text-align: center\" colspan=\"5\"><h3>" +
                    eventCategory.getCategory().getName() +
                    " - 3er Puesto" +
                    "</h3></td>\n" +
                    "      </tr>\n";
                List<Game> games = gameRepository.findByEventCategoryAndClasif(eventCategory, "3er Puesto");
                for (Game game : games) {
                    body1 =
                        body1 +
                        "      <tr>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getTeamA().getName() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getPointsA().toString() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">-</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getPointsB().toString() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getTeamB().getName() +
                        "</td>\n" +
                        "      </tr>\n";
                }
            }
            for (EventCategory eventCategory : eventCategories) {
                body1 =
                    body1 +
                    "      <tr>\n" +
                    "        <td style=\"text-align: center\" colspan=\"5\"><h3>" +
                    eventCategory.getCategory().getName() +
                    " - Final" +
                    "</h3></td>\n" +
                    "      </tr>\n";
                List<Game> games = gameRepository.findByEventCategoryAndClasif(eventCategory, "Final");
                for (Game game : games) {
                    body1 =
                        body1 +
                        "      <tr>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getTeamA().getName() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getPointsA().toString() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">-</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getPointsB().toString() +
                        "</td>\n" +
                        "        <td style=\"text-align: center; vertical-align: middle;\">" +
                        game.getTeamB().getName() +
                        "</td>\n" +
                        "      </tr>\n";
                }
            }
        }

        String close = "    </tbody>\n" + "  </table>\n" + " </div>";

        htmlString = htmlString.replace("$title", title);
        htmlString = htmlString.replace("$scss", tempFileCss.toString());
        htmlString = htmlString.replace("$mainTitle", mainTitle);
        htmlString = htmlString.replace("$tableHeader", tableHeader);
        htmlString = htmlString.replace("$body", body1);
        htmlString = htmlString.replace("$close", close);

        //        Files.copy(Paths.get(ClassLoader.getSystemResource("templates/pdf/event.css").toURI()), tempFileCss, StandardCopyOption.REPLACE_EXISTING);
        Files.write(tempFile, htmlString.getBytes(StandardCharsets.UTF_8));
        HtmlConverter.convertToPdf(new FileInputStream(tempFile.toString()), new FileOutputStream(tempPDFFile.toString()));
        return tempPDFFile.toFile();
    }

    @Scheduled(cron = "${application.cronEventStatus}")
    public void updateEventStatus() {
        log.info("*** Inicio de Cierre de Eventos ***");
        log.info("Fecha Actual: " + LocalDate.now().toString());
        Optional<List<Event>> events = eventRepository.findByEndDateAndStatus(LocalDate.now(), Status.CREATED);
        if (events.isPresent()) {
            for (Event event : events.get()) {
                event.setStatus(Status.DONE);
                log.info("Evento: " + event.getName() + " Finalizado");
            }
        } else {
            log.info("no hay eventos a cerrar para el d??a actual");
        }
        log.info("*** Fin de Cierre de Eventos  ***");
    }
}
