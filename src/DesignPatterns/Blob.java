/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DesignPatterns;

/**
 *
 * @author Kieran
 */
public class Blob extends AntiPattern {

    public void blob() {
        identify();
        analyseContracts();
        removeRedundantAssociations();
        migrateAssociates();
        removeTransientAssociations();
    }

    public void identify() {
        /*
         Identify or categorize related attributes and operations according to contracts. These
         contracts should be cohesive in that they all directly relate to a common focus, behavior,
         or function within the overall system. For example, a library system architecture diagram
         is represented with a potential Blob class called LIBRARY. In the example shown in
         Figure 5.3, the LIBRARY class encapsulates the sum total of all the system’s
         functionality. Therefore, the first step is to identify cohesive sets of operations and
         attributes that represent contracts. In this case, we could gather operations related to
         catalog management, like Sort_Catalog and Search_Catalog, as shown in Figure 5.4.
         We could also identify all operations and attributes related to individual items, such as
         Print_Item, Delete_Item, and so on.
         */
    }

    public void analyseContracts() {
        /*
         The second step is to look for “natural homes” for these contract−based collections of
         functionality and then migrate them there. In this example, we gather operations related
         to catalogs and migrate them from the LIBRARY class and move them to the CATALOG
         class, as shown in Figure 5.5. We do the same with operations and attributes related to
         items, moving them to the ITEM class. This both simplifies the LIBRARY class and
         makes the ITEM and CATALOG classes more than simple encapsulated data tables. The
         result is a better object−oriented design.
         */
    }

    public void removeRedundantAssociations() {
        /*
         The third step is to remove all “far−coupled,” or redundant, indirect associations. In the
         example, the ITEM class is initially far−coupled to the LIBRARY class in that each item
         really belongs to a CATALOG, which in turn belongs to a LIBRARY.
         */
    }

    public void migrateAssociates() {
        /*
         Next, where appropriate, we migrate associates to derived classes to a common base
         class. In the example, once the far−coupling has been removed between the LIBRARY
         and ITEM classes, we need to migrate ITEMs to CATALOGs.
         */
    }

    public void removeTransientAssociations() {
        /*
         Finally, we remove all transient associations, replacing them as appropriate with type
         specifiers to attributes and operations arguments. In our example, a Check_Out_Item or
         a Search_For_Item would be a transient process, and could be moved into a separate
         transient class with local attributes that establish the specific location or search criteria for
         a specific instance of a check−out or search.
         */
    }
}
