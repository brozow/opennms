/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.controller.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opennms.web.api.Authentication;
import org.opennms.web.svclayer.inventory.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;


/**
 * <p>AdminRancidStatusController class.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
@SuppressWarnings("deprecation")
public class AdminRancidStatusController extends SimpleFormController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AdminRancidStatusController.class);


    InventoryService m_inventoryService;
        
    /**
     * <p>getInventoryService</p>
     *
     * @return a {@link org.opennms.web.svclayer.inventory.InventoryService} object.
     */
    public InventoryService getInventoryService() {
        return m_inventoryService;
    }

    /**
     * <p>setInventoryService</p>
     *
     * @param inventoryService a {@link org.opennms.web.svclayer.inventory.InventoryService} object.
     */
    public void setInventoryService(InventoryService inventoryService) {
        m_inventoryService = inventoryService;
    }

    /** {@inheritDoc} */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws ServletException, IOException, Exception {

        LOG.debug("AdminRancidStatusController ModelAndView onSubmit");

        AdminRancidRouterDbCommClass bean = (AdminRancidRouterDbCommClass) command;
                       
        LOG.debug("AdminRancidStatusController ModelAndView onSubmit setting state to device[{}] group[{}] status[{}]", bean.getDeviceName(), bean.getGroupName(), bean.getStatusName());

        if (request.isUserInRole(Authentication.ROLE_ADMIN)) {

            boolean done = m_inventoryService.switchStatus(bean.getGroupName(), bean.getDeviceName());
            if (!done){
                LOG.debug("AdminRancidStatusController ModelAndView onSubmit error while updating status for{}/{}", bean.getGroupName(), bean.getDeviceName());
            }
        }
        String redirectURL = request.getHeader("Referer");
        response.sendRedirect(redirectURL);
        return super.onSubmit(request, response, command, errors);
    }

    /** {@inheritDoc} */
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
        throws ServletException {
        LOG.debug("AdminRancidStatusController initBinder");
    }
    
}
