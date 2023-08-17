package org.ejprarediseases.vpdpbackend.hierarchy.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.Hierarchy;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.HierarchyWay;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.OrphaCodeHierarchyDto;
import org.ejprarediseases.vpdpbackend.utils.ObjectIOHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.ArrayList;
import java.util.List;

@Service
public class HierarchyService {
    @Value("${application.hierarchyUrl}")
    private String hierarchyUrl;

    /**
     * Retrieves hierarchical information from the OrphaNet database for the specified OrphaCode,
     * based on the provided list of ways (UP, DOWN, or both) and the maximum number of levels.
     *
     * @param orphaCode     The OrphaCode for which the hierarchy information is to be retrieved.
     * @param ways          A list of HierarchyWay enumerations indicating the direction(s) of hierarchy to retrieve.
     *                      Possible values are HierarchyWay.UP and HierarchyWay.DOWN.
     * @param numberOfLevels The maximum number of levels to retrieve for each specified way.
     * @return A Hierarchy object representing the hierarchical information based on the provided parameters.
     *         The Hierarchy object will contain the hierarchical data either upward, downward, or both,
     *         based on the specified directions in the 'ways' list.
     * @throws JsonProcessingException If an error occurs during JSON processing while deserializing the hierarchy data.
     *
     * @see Hierarchy
     * @see HierarchyWay
     * @see #getOrphaCodeHierarchyUp(String)
     * @see #getOrphaCodeHierarchyDown(String)
     * @see ObjectIOHandler#deserialize(String, Class)
     */
    public Hierarchy getOrphaCodeHierarchy(String orphaCode, List<HierarchyWay> ways, int numberOfLevels)
            throws JsonProcessingException {
        Hierarchy orphaCodeHierarchy = new Hierarchy();
        for (HierarchyWay way : ways) {
            switch (way) {
                case UP -> {
                    Hierarchy hierarchyUp = ObjectIOHandler.deserialize(
                            getOrphaCodeHierarchyUp(orphaCode), Hierarchy.class);
                    orphaCodeHierarchy.setApiVersion(hierarchyUp.getApiVersion());
                    orphaCodeHierarchy.setParents(hierarchyUp.getParents(numberOfLevels));
                }
                case DOWN -> {
                    Hierarchy hierarchyDown = ObjectIOHandler.deserialize(
                            getOrphaCodeHierarchyDown(orphaCode), Hierarchy.class);
                    orphaCodeHierarchy.setChilds(hierarchyDown.getChilds(numberOfLevels));
                }
            }
        }
        return orphaCodeHierarchy;
    }

    /**
     * Converts the provided hierarchical data from the OrphaNet database into a combined list of
     * OrphaCodeHierarchyDto objects representing both the upward and downward hierarchical information.
     *
     * @param hierarchy The hierarchical data retrieved from the OrphaNet database.
     * @return A list of OrphaCodeHierarchyDto objects representing both upward and downward hierarchical information.
     *         An empty list is returned if the provided hierarchy or its parent/child nodes are null or empty.
     *
     * @see OrphaCodeHierarchyDto
     * @see Hierarchy
     * @see HierarchyWay
     * @see #convertOrphaCodeHierarchyUpToListOfOrphaCodeDto(Hierarchy)
     * @see #convertOrphaCodeHierarchyDownToListOfOrphaCodeDto(Hierarchy)
     */
    public List<OrphaCodeHierarchyDto> convertOrphaCodeHierarchyToListOfOrphaCodeDto(Hierarchy hierarchy) {
        List<OrphaCodeHierarchyDto> orphaCodes = new ArrayList<>();
        orphaCodes.addAll(convertOrphaCodeHierarchyUpToListOfOrphaCodeDto(hierarchy));
        orphaCodes.addAll(convertOrphaCodeHierarchyDownToListOfOrphaCodeDto(hierarchy));
        return orphaCodes;
    }

    /**
     * Converts the provided hierarchical data (upward direction) from the OrphaNet database into a list of
     * OrphaCodeHierarchyDto objects.
     *
     * @param hierarchy The hierarchical data retrieved from the OrphaNet database.
     * @return A list of OrphaCodeHierarchyDto objects representing the hierarchical information
     * in the upward direction.
     *         An empty list is returned if the provided hierarchy or its parent nodes are null or empty.
     *
     * @see OrphaCodeHierarchyDto
     * @see Hierarchy
     * @see HierarchyWay
     */
    public List<OrphaCodeHierarchyDto> convertOrphaCodeHierarchyUpToListOfOrphaCodeDto(Hierarchy hierarchy) {
        List<OrphaCodeHierarchyDto> orphaCodes = new ArrayList<>();
        if(hierarchy.getParents() != null && hierarchy.getParents().size() > 0) {
            hierarchy.getParents().forEach(
                    parents -> {
                        parents.getParents().forEach(
                                parentParents -> {
                                    OrphaCodeHierarchyDto orphaCodeHierarchyDto = new OrphaCodeHierarchyDto();
                                    orphaCodeHierarchyDto.setLevel(parents.getLevel());
                                    orphaCodeHierarchyDto.setCode(parentParents.getCode());
                                    orphaCodeHierarchyDto.setLabel(parentParents.getLabel());
                                    orphaCodeHierarchyDto.setWay(HierarchyWay.UP);
                                    orphaCodes.add(orphaCodeHierarchyDto);
                                }
                        );
                    }
            );
        }
        return orphaCodes;
    }

    /**
     * Converts the provided hierarchical data (downward direction) from the OrphaNet database into a list of
     * OrphaCodeHierarchyDto objects.
     *
     * @param hierarchy The hierarchical data retrieved from the OrphaNet database.
     * @return A list of OrphaCodeHierarchyDto objects representing the hierarchical information
     * in the downward direction.
     * An empty list is returned if the provided hierarchy or its child nodes are null or empty.
     *
     * @see OrphaCodeHierarchyDto
     * @see Hierarchy
     * @see HierarchyWay
     */
    public List<OrphaCodeHierarchyDto> convertOrphaCodeHierarchyDownToListOfOrphaCodeDto(Hierarchy hierarchy) {
        List<OrphaCodeHierarchyDto> orphaCodes = new ArrayList<>();
        if(hierarchy.getChilds() != null && hierarchy.getChilds().size() > 0) {
            hierarchy.getChilds().forEach(
                    childs -> {
                        childs.getChilds().forEach(
                                childChilds -> {
                                    OrphaCodeHierarchyDto orphaCodeHierarchyDto = new OrphaCodeHierarchyDto();
                                    orphaCodeHierarchyDto.setLevel(childs.getLevel());
                                    orphaCodeHierarchyDto.setCode(childChilds.getCode());
                                    orphaCodeHierarchyDto.setLabel(childChilds.getLabel());
                                    orphaCodeHierarchyDto.setWay(HierarchyWay.DOWN);
                                    orphaCodes.add(orphaCodeHierarchyDto);
                                }
                        );
                    }
            );
        }
        return orphaCodes;
    }


    /**
     * Retrieves the hierarchical information from the OrphaNet database for the specified OrphaCode.
     *
     * @param orphaCode     The OrphaCode for which the hierarchy information is to be retrieved.
     * @return A JSON representation of the hierarchical information up the specified OrphaCode.
     * @throws WebClientException If an error occurs while making the WebClient request.
     *
     */
    public String getOrphaCodeHierarchyUp(String orphaCode) {
        WebClient client = WebClient.create();
        return client.get()
                .uri( hierarchyUrl + "?code=" + orphaCode + "&way=up")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Retrieves the hierarchical information from the OrphaNet database for the specified OrphaCode.
     *
     * @param orphaCode     The OrphaCode for which the hierarchy information is to be retrieved.
     * @return A JSON representation of the hierarchical information down the specified OrphaCode.
     * @throws WebClientException If an error occurs while making the WebClient request.
     *
     */
    public String getOrphaCodeHierarchyDown(String orphaCode) {
        WebClient client = WebClient.create();
        return client.get()
                .uri( hierarchyUrl + "?code=" + orphaCode + "&way=down")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }



}
